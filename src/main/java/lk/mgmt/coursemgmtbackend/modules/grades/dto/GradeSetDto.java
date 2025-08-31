package lk.mgmt.coursemgmtbackend.modules.grades.dto;

import jakarta.validation.constraints.NotNull;
import lk.mgmt.coursemgmtbackend.common.enums.Grade;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GradeSetDto {
    @NotNull private Long enrollmentId;
    @NotNull private Grade grade;

}
