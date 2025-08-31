package lk.mgmt.coursemgmtbackend.modules.offerings.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Setter
@Getter
public class ScheduleDto {
    @NotNull private DayOfWeek dayOfWeek;
    @NotNull @JsonFormat(pattern = "HH:mm") private LocalTime startTime;
    @NotNull @JsonFormat(pattern = "HH:mm") private LocalTime endTime;
    private String location;

}
