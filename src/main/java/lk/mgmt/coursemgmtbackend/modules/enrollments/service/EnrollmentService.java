package lk.mgmt.coursemgmtbackend.modules.enrollments.service;

import lk.mgmt.coursemgmtbackend.modules.enrollments.dto.*;
import org.springframework.data.domain.*;

public interface EnrollmentService {

    // student actions
    EnrollmentDto enroll(Long studentId, Long offeringId);
    void drop(Long studentId, Long enrollmentId);
    Page<EnrollmentDto> listMine(Long studentId, Pageable pageable);

    // instructor (their offerings only)
    Page<EnrollmentDto> listByInstructor(Long instructorId, Pageable pageable);
    Page<EnrollmentDto> listByOfferingForInstructor(Long offeringId, Long instructorId, Pageable pageable);
    EnrollmentDto updateForInstructor(Long enrollmentId, Long instructorId, EnrollmentUpdateDto dto);

    // admin
    Page<EnrollmentDto> listAll(Pageable pageable);
    Page<EnrollmentDto> listByOffering(Long offeringId, Pageable pageable);
    Page<EnrollmentDto> listByStudent(Long studentId, Pageable pageable);
    EnrollmentDto adminUpdate(Long enrollmentId, EnrollmentUpdateDto dto);
    EnrollmentDto adminCreate(Long studentId, Long offeringId);
    void adminDelete(Long enrollmentId);
}
