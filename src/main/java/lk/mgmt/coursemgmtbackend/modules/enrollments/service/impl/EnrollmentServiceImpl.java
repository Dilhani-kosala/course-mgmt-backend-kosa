package lk.mgmt.coursemgmtbackend.modules.enrollments.service.impl;

import lk.mgmt.coursemgmtbackend.common.enums.EnrollmentStatus;
import lk.mgmt.coursemgmtbackend.common.enums.OfferingStatus;
import lk.mgmt.coursemgmtbackend.common.enums.TermStatus;
import lk.mgmt.coursemgmtbackend.modules.courses.entity.CourseEntity;
import lk.mgmt.coursemgmtbackend.modules.courses.entity.CoursePrerequisiteEntity;
import lk.mgmt.coursemgmtbackend.modules.courses.repo.CourseRepository;
import lk.mgmt.coursemgmtbackend.modules.enrollments.dto.*;
import lk.mgmt.coursemgmtbackend.modules.enrollments.entity.EnrollmentEntity;
import lk.mgmt.coursemgmtbackend.modules.enrollments.mapper.EnrollmentMapper;
import lk.mgmt.coursemgmtbackend.modules.enrollments.repo.EnrollmentRepository;
import lk.mgmt.coursemgmtbackend.modules.enrollments.service.EnrollmentService;
import lk.mgmt.coursemgmtbackend.modules.offerings.entity.OfferingEntity;
import lk.mgmt.coursemgmtbackend.modules.offerings.entity.OfferingScheduleEntity;
import lk.mgmt.coursemgmtbackend.modules.offerings.repo.OfferingRepository;
import lk.mgmt.coursemgmtbackend.modules.users.entity.UserEntity;
import lk.mgmt.coursemgmtbackend.modules.users.repo.UserRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollments;
    private final OfferingRepository offerings;
    private final CourseRepository courses;
    private final UserRepository users;
    private final EnrollmentMapper mapper;

    public EnrollmentServiceImpl(EnrollmentRepository enrollments, OfferingRepository offerings,
                                 CourseRepository courses, UserRepository users, EnrollmentMapper mapper) {
        this.enrollments = enrollments; this.offerings = offerings;
        this.courses = courses; this.users = users; this.mapper = mapper;
    }

    // --------------------- student actions ---------------------

    @Override
    public EnrollmentDto enroll(Long studentId, Long offeringId) {
        UserEntity student = users.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        OfferingEntity off = offerings.findWithGraphById(offeringId)
                .orElseThrow(() -> new IllegalArgumentException("Offering not found"));

        // basic guard: role
        if (student.getRole() == null || !"STUDENT".equalsIgnoreCase(student.getRole().getCode()))
            throw new IllegalArgumentException("Only STUDENTs can enroll");

        // status guards
        if (off.getStatus() != OfferingStatus.OPEN)
            throw new IllegalArgumentException("Offering is not OPEN");
        if (off.getTerm().getStatus() != TermStatus.ENROLLING && off.getTerm().getStatus() != TermStatus.IN_PROGRESS)
            throw new IllegalArgumentException("Term not currently enrolling");

        // already exists?
        Optional<EnrollmentEntity> existing = enrollments.findByStudent_IdAndOffering_Id(studentId, offeringId);
        if (existing.isPresent()) {
            EnrollmentEntity e = existing.get();
            if (e.getStatus() == EnrollmentStatus.DROPPED) {
                // allow re-enroll if capacity and clashes ok
                validateCapacity(off);
                validatePrerequisites(studentId, off);
                validateScheduleConflicts(studentId, off);
                e.setStatus(EnrollmentStatus.ENROLLED);
                e.setGrade(null);
                return mapper.toDto(enrollments.save(e));
            }
            throw new IllegalArgumentException("Already enrolled or completed this offering");
        }

        // capacity, prereqs, clashes
        validateCapacity(off);
        validatePrerequisites(studentId, off);
        validateScheduleConflicts(studentId, off);

        EnrollmentEntity e = new EnrollmentEntity();
        e.setOffering(off);
        e.setStudent(student);
        e.setStatus(EnrollmentStatus.ENROLLED);
        e = enrollments.save(e);
        return mapper.toDto(e);
    }

    @Override
    public void drop(Long studentId, Long enrollmentId) {
        EnrollmentEntity e = enrollments.findById(enrollmentId)
                .orElseThrow(() -> new IllegalArgumentException("Enrollment not found"));
        if (!Objects.equals(e.getStudent().getId(), studentId))
            throw new IllegalArgumentException("Cannot drop another student's enrollment");
        if (e.getStatus() != EnrollmentStatus.ENROLLED)
            throw new IllegalArgumentException("Only ENROLLED enrollments can be dropped");
        e.setStatus(EnrollmentStatus.DROPPED);
        e.setGrade(null);
        enrollments.save(e);
    }

    @Override @Transactional(readOnly = true)
    public Page<EnrollmentDto> listMine(Long studentId, Pageable pageable) {
        return enrollments.findByStudent_Id(studentId, pageable).map(mapper::toDto);
    }

    // --------------------- instructor ---------------------

    @Override @Transactional(readOnly = true)
    public Page<EnrollmentDto> listByInstructor(Long instructorId, Pageable pageable) {
        return enrollments.findByOffering_Instructor_Id(instructorId, pageable).map(mapper::toDto);
    }

    @Override @Transactional(readOnly = true)
    public Page<EnrollmentDto> listByOfferingForInstructor(Long offeringId, Long instructorId, Pageable pageable) {
        OfferingEntity off = offerings.findWithGraphById(offeringId)
                .orElseThrow(() -> new IllegalArgumentException("Offering not found"));
        if (!Objects.equals(off.getInstructor().getId(), instructorId))
            throw new IllegalArgumentException("Not your offering");
        return enrollments.findByOffering_Id(offeringId, pageable).map(mapper::toDto);
    }

    @Override
    public EnrollmentDto updateForInstructor(Long enrollmentId, Long instructorId, EnrollmentUpdateDto dto) {
        EnrollmentEntity e = enrollments.findById(enrollmentId)
                .orElseThrow(() -> new IllegalArgumentException("Enrollment not found"));
        if (!Objects.equals(e.getOffering().getInstructor().getId(), instructorId))
            throw new IllegalArgumentException("Not your offering");

        if (dto.getStatus() != null) {
            // Instructors commonly set COMPLETED at term end; we allow ENROLLED->COMPLETED, ENROLLED->DROPPED as needed.
            e.setStatus(dto.getStatus());
            if (dto.getStatus() != EnrollmentStatus.COMPLETED) {
                e.setGrade(null);
            }
        }
        if (dto.getGrade() != null) {
            e.setGrade(dto.getGrade());
            if (e.getStatus() != EnrollmentStatus.COMPLETED) {
                e.setStatus(EnrollmentStatus.COMPLETED);
            }
        }
        return mapper.toDto(enrollments.save(e));
    }

    // --------------------- admin ---------------------

    @Override @Transactional(readOnly = true)
    public Page<EnrollmentDto> listAll(Pageable pageable) {
        return enrollments.findAll(pageable).map(mapper::toDto);
    }

    @Override @Transactional(readOnly = true)
    public Page<EnrollmentDto> listByOffering(Long offeringId, Pageable pageable) {
        return enrollments.findByOffering_Id(offeringId, pageable).map(mapper::toDto);
    }

    @Override @Transactional(readOnly = true)
    public Page<EnrollmentDto> listByStudent(Long studentId, Pageable pageable) {
        return enrollments.findByStudent_Id(studentId, pageable).map(mapper::toDto);
    }

    @Override
    public EnrollmentDto adminUpdate(Long enrollmentId, EnrollmentUpdateDto dto) {
        EnrollmentEntity e = enrollments.findById(enrollmentId)
                .orElseThrow(() -> new IllegalArgumentException("Enrollment not found"));
        if (dto.getStatus() != null) {
            e.setStatus(dto.getStatus());
            if (dto.getStatus() != EnrollmentStatus.COMPLETED) e.setGrade(null);
        }
        if (dto.getGrade() != null) {
            e.setGrade(dto.getGrade());
            if (e.getStatus() != EnrollmentStatus.COMPLETED) e.setStatus(EnrollmentStatus.COMPLETED);
        }
        return mapper.toDto(enrollments.save(e));
    }

    @Override
    public EnrollmentDto adminCreate(Long studentId, Long offeringId) {
        return enroll(studentId, offeringId);
    }

    @Override
    public void adminDelete(Long enrollmentId) {
        enrollments.deleteById(enrollmentId);
    }

    // --------------------- validators ---------------------

    private void validateCapacity(OfferingEntity off) {
        long active = enrollments.countByOffering_IdAndStatus(off.getId(), EnrollmentStatus.ENROLLED);
        if (active >= off.getCapacity()) throw new IllegalArgumentException("Offering is full");
    }

    private void validatePrerequisites(Long studentId, OfferingEntity off) {
        CourseEntity course = off.getCourse();
        if (course.getPrerequisites() == null || course.getPrerequisites().isEmpty()) return;
        List<Long> requiredCourseIds = course.getPrerequisites().stream()
                .map(CoursePrerequisiteEntity::getPrerequisite)
                .map(CourseEntity::getId)
                .distinct().toList();
        long done = enrollments.countCompletedForCourses(studentId, requiredCourseIds);
        if (done < requiredCourseIds.size())
            throw new IllegalArgumentException("Missing prerequisite(s)");
    }

    private void validateScheduleConflicts(Long studentId, OfferingEntity target) {
        var sameTerm = enrollments.findByStudent_IdAndOffering_Term_IdAndStatus(
                studentId, target.getTerm().getId(), EnrollmentStatus.ENROLLED);

        if (sameTerm.isEmpty() || target.getSchedules() == null || target.getSchedules().isEmpty()) return;

        // Build day -> intervals for target
        Map<DayOfWeek, List<TimeRange>> targetMap = toMap(target.getSchedules());

        for (var e : sameTerm) {
            var other = e.getOffering();
            if (other.getSchedules() == null) continue;
            var otherMap = toMap(other.getSchedules());
            for (var day : targetMap.keySet()) {
                var a = targetMap.get(day);
                var b = otherMap.getOrDefault(day, List.of());
                for (var x : a) for (var y : b) {
                    if (x.overlaps(y)) {
                        throw new IllegalArgumentException("Schedule conflict with offering " + other.getId() + " on " + day);
                    }
                }
            }
        }
    }

    private Map<DayOfWeek, List<TimeRange>> toMap(Collection<OfferingScheduleEntity> blocks) {
        Map<DayOfWeek, List<TimeRange>> map = new HashMap<>();
        for (var s : blocks) {
            map.computeIfAbsent(s.getDayOfWeek(), k -> new ArrayList<>())
                    .add(new TimeRange(s.getStartTime(), s.getEndTime()));
        }
        for (var list : map.values()) list.sort(Comparator.comparing(tr -> tr.start));
        return map;
    }

    private record TimeRange(LocalTime start, LocalTime end) {
        boolean overlaps(TimeRange o) {
            // [start, end) style
            return !this.end.isBefore(o.start) && !o.end.isBefore(this.start);
        }
    }
}
