package lk.mgmt.coursemgmtbackend.modules.departments.service.impl;

import lk.mgmt.coursemgmtbackend.common.enums.DepartmentStatus;
import lk.mgmt.coursemgmtbackend.modules.departments.dto.*;
import lk.mgmt.coursemgmtbackend.modules.departments.entity.DepartmentEntity;
import lk.mgmt.coursemgmtbackend.modules.departments.mapper.DepartmentMapper;
import lk.mgmt.coursemgmtbackend.modules.departments.repo.DepartmentRepository;
import lk.mgmt.coursemgmtbackend.modules.departments.service.DepartmentService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository repo;
    private final DepartmentMapper mapper;

    public DepartmentServiceImpl(DepartmentRepository repo, DepartmentMapper mapper) {
        this.repo = repo; this.mapper = mapper;
    }

    @Override
    public DepartmentDto create(DepartmentCreateDto dto) {
        String code = dto.getCode().trim();
        if (repo.existsByCodeIgnoreCase(code)) {
            throw new IllegalArgumentException("Department code already exists");
        }
        DepartmentEntity e = new DepartmentEntity();
        e.setCode(code);
        e.setName(dto.getName().trim());
        e.setStatus(DepartmentStatus.ACTIVE);
        return mapper.toDto(repo.save(e));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DepartmentDto> list(String q, Pageable pageable) {
        Page<DepartmentEntity> page = (q == null || q.isBlank())
                ? repo.findAll(pageable)
                : repo.findByCodeContainingIgnoreCaseOrNameContainingIgnoreCase(q, q, pageable);
        return page.map(mapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentDto findOne(Long id) {
        DepartmentEntity e = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Department not found"));
        return mapper.toDto(e);
    }

    @Override
    public DepartmentDto update(Long id, DepartmentUpdateDto dto) {
        DepartmentEntity e = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Department not found"));

        if (dto.getCode() != null && !dto.getCode().isBlank()) {
            String newCode = dto.getCode().trim();
            if (!newCode.equalsIgnoreCase(e.getCode()) && repo.existsByCodeIgnoreCase(newCode)) {
                throw new IllegalArgumentException("Department code already exists");
            }
            e.setCode(newCode);
        }
        if (dto.getName() != null && !dto.getName().isBlank()) {
            e.setName(dto.getName().trim());
        }
        if (dto.getStatus() != null) {
            e.setStatus(dto.getStatus());
        }
        return mapper.toDto(repo.save(e));
    }

    @Override
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new IllegalArgumentException("Department not found");
        repo.deleteById(id);
    }
}
