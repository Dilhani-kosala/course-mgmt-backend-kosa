package lk.mgmt.coursemgmtbackend.modules.courses.service.impl;

import lk.mgmt.coursemgmtbackend.modules.courses.dto.*;
import lk.mgmt.coursemgmtbackend.modules.courses.entity.*;
import lk.mgmt.coursemgmtbackend.modules.courses.mapper.CourseMapper;
import lk.mgmt.coursemgmtbackend.modules.courses.repo.*;
import lk.mgmt.coursemgmtbackend.modules.courses.service.CourseService;
import lk.mgmt.coursemgmtbackend.modules.departments.entity.DepartmentEntity;
import lk.mgmt.coursemgmtbackend.modules.departments.repo.DepartmentRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courses;
    private final CoursePrerequisiteRepository prereqs;
    private final DepartmentRepository departments;
    private final CourseMapper mapper;

    public CourseServiceImpl(CourseRepository courses, CoursePrerequisiteRepository prereqs,
                             DepartmentRepository departments, CourseMapper mapper) {
        this.courses = courses; this.prereqs = prereqs; this.departments = departments; this.mapper = mapper;
    }

    @Override
    public CourseDto create(CourseCreateDto dto) {
        String code = dto.getCode().trim();
        if (courses.existsByCodeIgnoreCase(code))
            throw new IllegalArgumentException("Course code already exists");

        DepartmentEntity dept = departments.findById(dto.getDepartmentId())
                .orElseThrow(() -> new IllegalArgumentException("Department not found"));

        CourseEntity e = new CourseEntity();
        e.setCode(code);
        e.setTitle(dto.getTitle().trim());
        e.setDescription(dto.getDescription());
        e.setCredits(dto.getCredits());
        e.setDepartment(dept);

        // save first to get ID for composite PKs
        e = courses.save(e);

        if (dto.getPrerequisiteIds() != null && !dto.getPrerequisiteIds().isEmpty()) {
            setPrerequisites(e, dto.getPrerequisiteIds());
            e = courses.findWithDeptAndPrereqsById(e.getId()).orElse(e); // refresh with graph
        }

        return mapper.toDto(e);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CourseDto> list(String q, Long departmentId, Pageable pageable) {
        Page<CourseEntity> page = courses.search(
                (q == null || q.isBlank()) ? null : q.trim(),
                departmentId,
                pageable
        );
        return page.map(mapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public CourseDto findOne(Long id) {
        CourseEntity e = courses.findWithDeptAndPrereqsById(id)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));
        return mapper.toDto(e);
    }

    @Override
    public CourseDto update(Long id, CourseUpdateDto dto) {
        CourseEntity e = courses.findWithDeptAndPrereqsById(id)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        if (dto.getCode() != null && !dto.getCode().isBlank()) {
            String newCode = dto.getCode().trim();
            if (!newCode.equalsIgnoreCase(e.getCode()) && courses.existsByCodeIgnoreCase(newCode))
                throw new IllegalArgumentException("Course code already exists");
            e.setCode(newCode);
        }
        if (dto.getTitle() != null && !dto.getTitle().isBlank()) e.setTitle(dto.getTitle().trim());
        if (dto.getDescription() != null) e.setDescription(dto.getDescription());
        if (dto.getCredits() != null) e.setCredits(dto.getCredits());

        if (dto.getDepartmentId() != null) {
            DepartmentEntity dept = departments.findById(dto.getDepartmentId())
                    .orElseThrow(() -> new IllegalArgumentException("Department not found"));
            e.setDepartment(dept);
        }

        // Replace full prerequisite set if provided
        if (dto.getPrerequisiteIds() != null) {
            setPrerequisites(e, dto.getPrerequisiteIds());
        }

        e = courses.save(e);
        return mapper.toDto(e);
    }

    @Override
    public void delete(Long id) {
        // Prevent delete if someone else depends on this as a prerequisite
        if (prereqs.existsByPrerequisite_Id(id))
            throw new IllegalArgumentException("Cannot delete: course is a prerequisite of other courses");
        // Remove its own prereq rows
        prereqs.deleteByCourse_Id(id);
        courses.deleteById(id);
    }

    private void setPrerequisites(CourseEntity course, List<Long> prereqIds) {
        // Clear existing
        prereqs.deleteByCourse_Id(course.getId());
        course.getPrerequisites().clear();

        if (prereqIds.isEmpty()) return;

        // Validate: no self, unique set
        Set<Long> unique = new LinkedHashSet<>(prereqIds);
        if (unique.contains(course.getId()))
            throw new IllegalArgumentException("A course cannot be its own prerequisite");

        // Load all referenced courses
        Map<Long, CourseEntity> map = new HashMap<>();
        for (Long pid : unique) {
            CourseEntity p = courses.findById(pid)
                    .orElseThrow(() -> new IllegalArgumentException("Prerequisite course not found: " + pid));
            map.put(pid, p);
        }

        // Create rows
        for (Long pid : unique) {
            CoursePrerequisiteEntity link = new CoursePrerequisiteEntity(course, map.get(pid));
            course.getPrerequisites().add(link);
            prereqs.save(link);
        }
    }
}
