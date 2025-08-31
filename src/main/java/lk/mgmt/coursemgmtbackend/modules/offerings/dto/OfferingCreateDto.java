package lk.mgmt.coursemgmtbackend.modules.offerings.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class OfferingCreateDto {
    @NotNull private Long courseId;
    @NotNull private Long termId;
    @NotNull private Long instructorId;

    @NotBlank @Size(max = 16) private String section;
    @NotNull @Min(1) private Integer capacity;

    private List<@NotNull ScheduleDto> schedules;

}
