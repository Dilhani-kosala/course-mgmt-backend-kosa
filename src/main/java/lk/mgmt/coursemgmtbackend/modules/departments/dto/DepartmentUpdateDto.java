package lk.mgmt.coursemgmtbackend.modules.departments.dto;

import jakarta.validation.constraints.Size;
import lk.mgmt.coursemgmtbackend.common.enums.DepartmentStatus;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DepartmentUpdateDto {
    // getters/setters
    @Size(max = 32)
    private String code;  // optional; unique if changed
    @Size(max = 120)
    private String name;  // optional
    private DepartmentStatus status; // ACTIVE/INACTIVE

}
