package lk.mgmt.coursemgmtbackend.modules.terms.service;

import lk.mgmt.coursemgmtbackend.modules.terms.dto.*;
import org.springframework.data.domain.*;

import java.time.LocalDate;

public interface TermService {
    TermDto create(TermCreateDto dto);
    Page<TermDto> list(String q, LocalDate from, LocalDate to, Pageable pageable);
    TermDto findOne(Long id);
    TermDto update(Long id, TermUpdateDto dto);
    void delete(Long id);
}
