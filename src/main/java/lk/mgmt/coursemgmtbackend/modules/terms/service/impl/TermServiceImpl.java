package lk.mgmt.coursemgmtbackend.modules.terms.service.impl;

import lk.mgmt.coursemgmtbackend.common.enums.TermStatus;
import lk.mgmt.coursemgmtbackend.modules.terms.dto.*;
import lk.mgmt.coursemgmtbackend.modules.terms.entity.TermEntity;
import lk.mgmt.coursemgmtbackend.modules.terms.mapper.TermMapper;
import lk.mgmt.coursemgmtbackend.modules.terms.repo.TermRepository;
import lk.mgmt.coursemgmtbackend.modules.terms.service.TermService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
public class TermServiceImpl implements TermService {

    private final TermRepository repo;
    private final TermMapper mapper;

    public TermServiceImpl(TermRepository repo, TermMapper mapper) {
        this.repo = repo; this.mapper = mapper;
    }

    @Override
    public TermDto create(TermCreateDto dto) {
        String code = dto.getCode().trim();
        if (repo.existsByCodeIgnoreCase(code))
            throw new IllegalArgumentException("Term code already exists");

        validateDates(dto.getStartDate(), dto.getEndDate());
        ensureNoOverlap(dto.getStartDate(), dto.getEndDate(), null);

        TermEntity e = new TermEntity();
        e.setCode(code);
        e.setName(dto.getName().trim());
        e.setStartDate(dto.getStartDate());
        e.setEndDate(dto.getEndDate());
        e.setStatus(TermStatus.PLANNED);

        return mapper.toDto(repo.save(e));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TermDto> list(String q, LocalDate from, LocalDate to, Pageable pageable) {
        return repo.search((q == null || q.isBlank()) ? null : q.trim(), from, to, pageable)
                .map(mapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public TermDto findOne(Long id) {
        TermEntity e = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Term not found"));
        return mapper.toDto(e);
    }

    @Override
    public TermDto update(Long id, TermUpdateDto dto) {
        TermEntity e = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Term not found"));

        if (dto.getCode() != null && !dto.getCode().isBlank()) {
            String newCode = dto.getCode().trim();
            if (!newCode.equalsIgnoreCase(e.getCode()) && repo.existsByCodeIgnoreCase(newCode))
                throw new IllegalArgumentException("Term code already exists");
            e.setCode(newCode);
        }
        if (dto.getName() != null && !dto.getName().isBlank())
            e.setName(dto.getName().trim());

        LocalDate start = dto.getStartDate() != null ? dto.getStartDate() : e.getStartDate();
        LocalDate end   = dto.getEndDate()   != null ? dto.getEndDate()   : e.getEndDate();

        if (dto.getStartDate() != null || dto.getEndDate() != null) {
            validateDates(start, end);
            ensureNoOverlap(start, end, e.getId());
            e.setStartDate(start);
            e.setEndDate(end);
        }

        if (dto.getStatus() != null) e.setStatus(dto.getStatus());

        return mapper.toDto(repo.save(e));
    }

    @Override
    public void delete(Long id) {
        // (Later we may check offerings referencing this term.)
        if (!repo.existsById(id)) throw new IllegalArgumentException("Term not found");
        repo.deleteById(id);
    }

    private void validateDates(LocalDate start, LocalDate end) {
        if (start == null || end == null) throw new IllegalArgumentException("Start and end dates are required");
        if (end.isBefore(start)) throw new IllegalArgumentException("End date must be on or after start date");
    }

    private void ensureNoOverlap(LocalDate start, LocalDate end, Long excludeId) {
        long cnt = repo.countOverlapping(start, end, excludeId);
        if (cnt > 0) throw new IllegalArgumentException("Term dates overlap with an existing term");
    }
}
