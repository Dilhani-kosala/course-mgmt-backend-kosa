package lk.mgmt.coursemgmtbackend.modules.courses.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class CourseCreateDto {
    // getters/setters
    @NotBlank @Size(max = 32)
    private String code;

    @NotBlank @Size(max = 200)
    private String title;

    @Size(max = 2000)
    private String description;

    @NotNull @DecimalMin("0.5") @DecimalMax("10.0")
    private Double credits;

    @NotNull
    private Long departmentId;

    // optional initial prerequisites
    private List<Long> prerequisiteIds;

}
