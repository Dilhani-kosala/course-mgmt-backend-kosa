package lk.mgmt.coursemgmtbackend.modules.enrollments.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EnrollRequestDto {
    @NotNull
    private Long offeringId;

}
