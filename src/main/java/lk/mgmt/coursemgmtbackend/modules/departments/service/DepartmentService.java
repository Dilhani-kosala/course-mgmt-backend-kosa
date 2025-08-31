package lk.mgmt.coursemgmtbackend.modules.departments.service;

import lk.mgmt.coursemgmtbackend.modules.departments.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DepartmentService {
    DepartmentDto create(DepartmentCreateDto dto);
    Page<DepartmentDto> list(String q, Pageable pageable);
    DepartmentDto findOne(Long id);
    DepartmentDto update(Long id, DepartmentUpdateDto dto);
    void delete(Long id);
}
