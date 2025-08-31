package lk.mgmt.coursemgmtbackend.modules.offerings.dto;

import jakarta.validation.constraints.Min;
import lk.mgmt.coursemgmtbackend.common.enums.OfferingStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class InstructorOfferingUpdateDto {
    @Min(1)
    private Integer capacity;          // optional
    private OfferingStatus status;     // optional (PLANNED/OPEN/CLOSED/CANCELLED)
    private List<ScheduleDto> schedules; // optional; if present, replaces all

}
