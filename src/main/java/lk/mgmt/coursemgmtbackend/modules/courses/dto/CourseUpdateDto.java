package lk.mgmt.coursemgmtbackend.modules.courses.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class CourseUpdateDto {
    // getters/setters
    @Size(max = 32)  private String code;
    @Size(max = 200) private String title;
    @Size(max = 2000) private String description;
    @DecimalMin("0.5") @DecimalMax("10.0")
    private Double credits;
    private Long departmentId;

    // if provided, replaces the full prerequisite set
    private List<Long> prerequisiteIds;

}
