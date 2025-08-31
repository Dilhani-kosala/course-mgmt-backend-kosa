package lk.mgmt.coursemgmtbackend.modules.departments.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DepartmentCreateDto {
    // getters/setters
    @NotBlank @Size(max = 32)
    private String code;

    @NotBlank @Size(max = 120)
    private String name;

}
