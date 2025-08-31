package lk.mgmt.coursemgmtbackend.modules.departments.dto;

import lk.mgmt.coursemgmtbackend.common.enums.DepartmentStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class DepartmentDto {
    // getters/setters
    private Long id;
    private String code;
    private String name;
    private DepartmentStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
