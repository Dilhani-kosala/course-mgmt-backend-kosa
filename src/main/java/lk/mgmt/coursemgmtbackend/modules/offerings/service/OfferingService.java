package lk.mgmt.coursemgmtbackend.modules.offerings.service;

import lk.mgmt.coursemgmtbackend.modules.offerings.dto.*;
import org.springframework.data.domain.*;

public interface OfferingService {
    OfferingDto create(OfferingCreateDto dto);
    Page<OfferingDto> list(String q, Long termId, Long courseId, Long instructorId, Pageable pageable);
    OfferingDto findOne(Long id);
    OfferingDto update(Long id, OfferingUpdateDto dto);
    void delete(Long id);

    Page<OfferingDto> listMine(String q, Long termId, Long courseId, Pageable pageable, Long instructorId);
    OfferingDto getMine(Long id, Long instructorId);
    OfferingDto updateByInstructor(Long id, InstructorOfferingUpdateDto dto, Long instructorId);

}
