package lk.mgmt.coursemgmtbackend.modules.offerings.dto;

import lk.mgmt.coursemgmtbackend.common.enums.OfferingStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class OfferingDto {
    // getters/setters
    private Long id;
    private String section;
    private Integer capacity;
    private OfferingStatus status;

    private Long courseId;
    private String courseCode;
    private String courseTitle;

    private Long termId;
    private String termCode;
    private String termName;

    private Long instructorId;
    private String instructorName;
    private String instructorEmail;

    private List<ScheduleDto> schedules;

}
