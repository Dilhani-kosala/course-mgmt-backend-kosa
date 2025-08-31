package lk.mgmt.coursemgmtbackend.modules.grades.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class BulkGradeSetDto {
    @NotNull private Long offeringId;               // the instructor's offering
    @NotNull private List<GradeSetDto> items;       // enrollmentId + grade

}
