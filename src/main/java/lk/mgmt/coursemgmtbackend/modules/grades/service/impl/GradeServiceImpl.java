package lk.mgmt.coursemgmtbackend.modules.grades.service.impl;

import lk.mgmt.coursemgmtbackend.common.enums.EnrollmentStatus;
import lk.mgmt.coursemgmtbackend.common.enums.Grade;
import lk.mgmt.coursemgmtbackend.common.util.GradePoints;
import lk.mgmt.coursemgmtbackend.modules.enrollments.entity.EnrollmentEntity;
import lk.mgmt.coursemgmtbackend.modules.enrollments.repo.EnrollmentRepository;
import lk.mgmt.coursemgmtbackend.modules.grades.dto.*;
import lk.mgmt.coursemgmtbackend.modules.grades.service.GradeService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class GradeServiceImpl implements GradeService
{
    private final EnrollmentRepository enrollments;

    public GradeServiceImpl(EnrollmentRepository enrollments) {
        this.enrollments = enrollments;
    }

    // ---------- instructor grading ----------

    @Override
    public void setGradeByInstructor(Long instructorId, Long enrollmentId, Grade grade) {
        EnrollmentEntity e = enrollments.findById(enrollmentId)
                .orElseThrow(() -> new IllegalArgumentException("Enrollment not found"));
        if (!Objects.equals(e.getOffering().getInstructor().getId(), instructorId))
            throw new IllegalArgumentException("Not your offering");
        applyGrade(e, grade);
    }

    @Override
    public void bulkSetGradesByInstructor(Long instructorId, BulkGradeSetDto dto) {
        // load all active enrollments in that offering for safety
        var list = enrollments.findByOffering_IdAndStatus(dto.getOfferingId(), EnrollmentStatus.ENROLLED);
        // map by id for quick access
        Map<Long, EnrollmentEntity> map = list.stream()
                .filter(e -> Objects.equals(e.getOffering().getInstructor().getId(), instructorId))
                .collect(Collectors.toMap(EnrollmentEntity::getId, e -> e));

        for (var item : dto.getItems()) {
            EnrollmentEntity e = map.get(item.getEnrollmentId());
            if (e == null)
                throw new IllegalArgumentException("Enrollment not found or not yours: " + item.getEnrollmentId());
            applyGrade(e, item.getGrade());
        }
    }

    // ---------- admin override ----------

    @Override
    public void setGradeByAdmin(Long enrollmentId, Grade grade) {
        EnrollmentEntity e = enrollments.findById(enrollmentId)
                .orElseThrow(() -> new IllegalArgumentException("Enrollment not found"));
        applyGrade(e, grade);
    }

    private void applyGrade(EnrollmentEntity e, Grade grade) {
        if (e.getStatus() == EnrollmentStatus.DROPPED)
            throw new IllegalArgumentException("Cannot grade a DROPPED enrollment");
        e.setGrade(grade);
        e.setStatus(EnrollmentStatus.COMPLETED); // grading completes the enrollment
        enrollments.save(e);
    }

    // ---------- transcript & GPA ----------

    @Override
    @Transactional(readOnly = true)
    public TranscriptDto getTranscript(Long studentId, Pageable linesPage) {
        var completed = enrollments.findByStudent_IdAndStatus(studentId, EnrollmentStatus.COMPLETED);
        return buildTranscript(completed);
    }

    @Override
    @Transactional(readOnly = true)
    public TranscriptDto getTranscriptForTerm(Long studentId, Long termId) {
        var completed = enrollments.findByStudent_IdAndOffering_Term_IdAndStatus(
                studentId, termId, EnrollmentStatus.COMPLETED);
        return buildTranscript(completed);
    }

    private TranscriptDto buildTranscript(List<EnrollmentEntity> completed) {
        TranscriptDto dto = new TranscriptDto();

        // lines
        var lines = new ArrayList<TranscriptLineDto>();
        double totalQuality = 0.0, totalAttempted = 0.0, totalEarned = 0.0;

        // per-term aggregates
        Map<String, Double> termQuality = new LinkedHashMap<>();
        Map<String, Double> termAttempted = new LinkedHashMap<>();
        Map<String, Double> termEarned = new LinkedHashMap<>();

        for (var e : completed) {
            var off = e.getOffering();
            var course = off.getCourse();
            var term = off.getTerm();

            double credits = course.getCredits() == null ? 0.0 : course.getCredits();
            double points = GradePoints.toPoints(e.getGrade());
            boolean earned = GradePoints.earnsCredit(e.getGrade());

            var line = new TranscriptLineDto();
            line.setTermCode(term.getCode());
            line.setCourseCode(course.getCode());
            line.setCourseTitle(course.getTitle());
            line.setCredits(credits);
            line.setGrade(e.getGrade() != null ? e.getGrade().name() : null);
            lines.add(line);

            totalQuality += points * credits;
            totalAttempted += credits;
            if (earned) totalEarned += credits;

            termQuality.merge(term.getCode(), points * credits, Double::sum);
            termAttempted.merge(term.getCode(), credits, Double::sum);
            termEarned.merge(term.getCode(), earned ? credits : 0.0, Double::sum);
        }

        // term GPA
        Map<String, TranscriptDto.TermGpaDto> termGpa = new LinkedHashMap<>();
        for (var tc : termAttempted.keySet()) {
            double att = termAttempted.getOrDefault(tc, 0.0);
            double qual = termQuality.getOrDefault(tc, 0.0);
            double ern = termEarned.getOrDefault(tc, 0.0);
            TranscriptDto.TermGpaDto g = new TranscriptDto.TermGpaDto();
            g.setCreditsAttempted(att);
            g.setCreditsEarned(ern);
            g.setGpa(att > 0 ? round2(qual / att) : 0.0);
            termGpa.put(tc, g);
        }

        dto.setLines(lines);
        dto.setTermGpa(termGpa);
        dto.setTotalCreditsAttempted(totalAttempted);
        dto.setTotalCreditsEarned(totalEarned);
        dto.setCumulativeGpa(totalAttempted > 0 ? round2(totalQuality / totalAttempted) : 0.0);
        return dto;
    }

    private double round2(double v) {
        return Math.round(v * 100.0) / 100.0;
    }
}
