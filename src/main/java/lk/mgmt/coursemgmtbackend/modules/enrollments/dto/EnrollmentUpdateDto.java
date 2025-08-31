package lk.mgmt.coursemgmtbackend.modules.enrollments.dto;

import lk.mgmt.coursemgmtbackend.common.enums.EnrollmentStatus;
import lk.mgmt.coursemgmtbackend.common.enums.Grade;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EnrollmentUpdateDto {
    private EnrollmentStatus status; // allow ADMIN/INSTRUCTOR to set
    private Grade grade;             // optional

}
