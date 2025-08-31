package lk.mgmt.coursemgmtbackend.modules.enrollments.dto;

import lk.mgmt.coursemgmtbackend.common.enums.EnrollmentStatus;
import lk.mgmt.coursemgmtbackend.common.enums.Grade;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EnrollmentDto {
    // getters/setters
    private Long id;

    // student (minimal)
    private Long studentId;
    private String studentName;
    private String studentEmail;

    // offering summary
    private Long offeringId;
    private String courseCode;
    private String courseTitle;
    private String termCode;
    private String section;
    private Integer capacity;

    private EnrollmentStatus status;
    private Grade grade;

}
