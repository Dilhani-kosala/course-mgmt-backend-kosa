package lk.mgmt.coursemgmtbackend.modules.courses.service;

import lk.mgmt.coursemgmtbackend.modules.courses.dto.*;
import org.springframework.data.domain.*;

public interface CourseService {
    CourseDto create(CourseCreateDto dto);
    Page<CourseDto> list(String q, Long departmentId, Pageable pageable);
    CourseDto findOne(Long id);
    CourseDto update(Long id, CourseUpdateDto dto);
    void delete(Long id);
}
