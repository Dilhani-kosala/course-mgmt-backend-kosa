package lk.mgmt.coursemgmtbackend.modules.offerings.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lk.mgmt.coursemgmtbackend.common.enums.OfferingStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class OfferingUpdateDto {
    private Long courseId;
    private Long termId;
    private Long instructorId;

    @Size(max = 16) private String section;
    @Min(1) private Integer capacity;

    private OfferingStatus status;

    // If present, replaces the schedules fully
    private List<ScheduleDto> schedules;

}
