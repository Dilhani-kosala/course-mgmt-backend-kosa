package lk.mgmt.coursemgmtbackend.modules.offerings.service.impl;

import lk.mgmt.coursemgmtbackend.common.enums.OfferingStatus;
import lk.mgmt.coursemgmtbackend.modules.courses.entity.CourseEntity;
import lk.mgmt.coursemgmtbackend.modules.courses.repo.CourseRepository;
import lk.mgmt.coursemgmtbackend.modules.offerings.dto.*;
import lk.mgmt.coursemgmtbackend.modules.offerings.entity.*;
import lk.mgmt.coursemgmtbackend.modules.offerings.mapper.OfferingMapper;
import lk.mgmt.coursemgmtbackend.modules.offerings.repo.*;
import lk.mgmt.coursemgmtbackend.modules.offerings.service.OfferingService;
import lk.mgmt.coursemgmtbackend.modules.terms.entity.TermEntity;
import lk.mgmt.coursemgmtbackend.modules.terms.repo.TermRepository;
import lk.mgmt.coursemgmtbackend.modules.users.entity.UserEntity;
import lk.mgmt.coursemgmtbackend.modules.users.repo.UserRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.*;

@Service
@Transactional
public class OfferingServiceImpl implements OfferingService {

    private final OfferingRepository offerings;
    private final OfferingScheduleRepository schedules;
    private final CourseRepository courses;
    private final TermRepository terms;
    private final UserRepository users;
    private final OfferingMapper mapper;

    public OfferingServiceImpl(OfferingRepository offerings, OfferingScheduleRepository schedules,
                               CourseRepository courses, TermRepository terms,
                               UserRepository users, OfferingMapper mapper) {
        this.offerings = offerings; this.schedules = schedules;
        this.courses = courses; this.terms = terms; this.users = users; this.mapper = mapper;
    }

    @Override
    public OfferingDto create(OfferingCreateDto dto) {
        CourseEntity course = courses.findById(dto.getCourseId())
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));
        TermEntity term = terms.findById(dto.getTermId())
                .orElseThrow(() -> new IllegalArgumentException("Term not found"));
        UserEntity instructor = users.findById(dto.getInstructorId())
                .orElseThrow(() -> new IllegalArgumentException("Instructor not found"));

        // basic role guard (expects Role.code = INSTRUCTOR)
        if (instructor.getRole() == null || !"INSTRUCTOR".equalsIgnoreCase(instructor.getRole().getCode()))
            throw new IllegalArgumentException("User is not an INSTRUCTOR");

        if (offerings.existsByTerm_IdAndCourse_IdAndSectionIgnoreCase(term.getId(), course.getId(), dto.getSection()))
            throw new IllegalArgumentException("Offering section already exists for this term & course");

        if (dto.getCapacity() == null || dto.getCapacity() < 1)
            throw new IllegalArgumentException("Capacity must be >= 1");

        OfferingEntity e = new OfferingEntity();
        e.setCourse(course);
        e.setTerm(term);
        e.setInstructor(instructor);
        e.setSection(dto.getSection().trim());
        e.setCapacity(dto.getCapacity());
        e.setStatus(OfferingStatus.PLANNED);

        e = offerings.save(e);

        if (dto.getSchedules() != null) replaceSchedules(e, dto.getSchedules());

        return mapper.toDto(offerings.findWithGraphById(e.getId()).orElse(e));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OfferingDto> list(String q, Long termId, Long courseId, Long instructorId, Pageable pageable) {
        return offerings.search((q == null || q.isBlank()) ? null : q.trim(),
                termId, courseId, instructorId, pageable).map(mapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public OfferingDto findOne(Long id) {
        OfferingEntity e = offerings.findWithGraphById(id)
                .orElseThrow(() -> new IllegalArgumentException("Offering not found"));
        return mapper.toDto(e);
    }

    @Override
    public OfferingDto update(Long id, OfferingUpdateDto dto) {
        OfferingEntity e = offerings.findWithGraphById(id)
                .orElseThrow(() -> new IllegalArgumentException("Offering not found"));

        if (dto.getCourseId() != null && !dto.getCourseId().equals(e.getCourse().getId())) {
            e.setCourse(courses.findById(dto.getCourseId())
                    .orElseThrow(() -> new IllegalArgumentException("Course not found")));
        }
        if (dto.getTermId() != null && !dto.getTermId().equals(e.getTerm().getId())) {
            e.setTerm(terms.findById(dto.getTermId())
                    .orElseThrow(() -> new IllegalArgumentException("Term not found")));
        }
        if (dto.getInstructorId() != null && !dto.getInstructorId().equals(e.getInstructor().getId())) {
            UserEntity instructor = users.findById(dto.getInstructorId())
                    .orElseThrow(() -> new IllegalArgumentException("Instructor not found"));
            if (instructor.getRole() == null || !"INSTRUCTOR".equalsIgnoreCase(instructor.getRole().getCode()))
                throw new IllegalArgumentException("User is not an INSTRUCTOR");
            e.setInstructor(instructor);
        }

        if (dto.getSection() != null && !dto.getSection().isBlank()) {
            String newSection = dto.getSection().trim();
            boolean dup = offerings.existsByTerm_IdAndCourse_IdAndSectionIgnoreCase(
                    e.getTerm().getId(), e.getCourse().getId(), newSection);
            if (dup && !newSection.equalsIgnoreCase(e.getSection()))
                throw new IllegalArgumentException("Offering section already exists for this term & course");
            e.setSection(newSection);
        }

        if (dto.getCapacity() != null) {
            if (dto.getCapacity() < 1) throw new IllegalArgumentException("Capacity must be >= 1");
            e.setCapacity(dto.getCapacity());
        }

        if (dto.getStatus() != null) e.setStatus(dto.getStatus());

        if (dto.getSchedules() != null) replaceSchedules(e, dto.getSchedules());

        e = offerings.save(e);
        return mapper.toDto(offerings.findWithGraphById(e.getId()).orElse(e));
    }

    @Override
    public void delete(Long id) {
        // later: guard if enrollments exist
        schedules.deleteByOffering_Id(id);
        offerings.deleteById(id);
    }

    // ----- helpers -----

    private void replaceSchedules(OfferingEntity offering, List<ScheduleDto> items) {
        // Clear existing
        schedules.deleteByOffering_Id(offering.getId());
        offering.getSchedules().clear();

        if (items == null || items.isEmpty()) return;

        // Validate overlaps (per day)
        Map<String, List<ScheduleDto>> byDay = new HashMap<>();
        for (ScheduleDto s : items) {
            if (s.getStartTime() == null || s.getEndTime() == null || s.getDayOfWeek() == null)
                throw new IllegalArgumentException("Schedule requires dayOfWeek, startTime, endTime");
            if (!s.getEndTime().isAfter(s.getStartTime()))
                throw new IllegalArgumentException("Schedule endTime must be after startTime");
            String key = s.getDayOfWeek().name();
            byDay.computeIfAbsent(key, k -> new ArrayList<>()).add(s);
        }
        for (var entry : byDay.entrySet()) {
            List<ScheduleDto> day = entry.getValue();
            day.sort(Comparator.comparing(ScheduleDto::getStartTime));
            for (int i = 1; i < day.size(); i++) {
                LocalTime prevEnd = day.get(i - 1).getEndTime();
                LocalTime curStart = day.get(i).getStartTime();
                if (!curStart.isAfter(prevEnd))
                    throw new IllegalArgumentException("Overlapping time blocks on " + entry.getKey());
            }
        }

        // Save
        for (ScheduleDto s : items) {
            OfferingScheduleEntity e = new OfferingScheduleEntity();
            e.setOffering(offering);
            e.setDayOfWeek(s.getDayOfWeek());
            e.setStartTime(s.getStartTime());
            e.setEndTime(s.getEndTime());
            e.setLocation(s.getLocation());
            offering.getSchedules().add(e);
            schedules.save(e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OfferingDto> listMine(String q, Long termId, Long courseId,
                                      Pageable pageable, Long instructorId) {
        return offerings.search((q == null || q.isBlank()) ? null : q.trim(),
                termId, courseId, instructorId, pageable).map(mapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public OfferingDto getMine(Long id, Long instructorId) {
        OfferingEntity e = offerings.findWithGraphById(id)
                .orElseThrow(() -> new IllegalArgumentException("Offering not found"));
        if (!Objects.equals(e.getInstructor().getId(), instructorId))
            throw new IllegalArgumentException("You do not own this offering");
        return mapper.toDto(e);
    }

    @Override
    public OfferingDto updateByInstructor(Long id, InstructorOfferingUpdateDto dto, Long instructorId) {
        OfferingEntity e = offerings.findWithGraphById(id)
                .orElseThrow(() -> new IllegalArgumentException("Offering not found"));
        if (!Objects.equals(e.getInstructor().getId(), instructorId))
            throw new IllegalArgumentException("You do not own this offering");

        if (dto.getCapacity() != null) {
            if (dto.getCapacity() < 1) throw new IllegalArgumentException("Capacity must be >= 1");
            e.setCapacity(dto.getCapacity());
        }
        if (dto.getStatus() != null) {
            // (Optional: enforce transitions you prefer. For now accept the enum.)
            e.setStatus(dto.getStatus());
        }
        if (dto.getSchedules() != null) {
            replaceSchedules(e, dto.getSchedules()); // reuse the existing helper
        }

        e = offerings.save(e);
        return mapper.toDto(offerings.findWithGraphById(e.getId()).orElse(e));
    }

}
