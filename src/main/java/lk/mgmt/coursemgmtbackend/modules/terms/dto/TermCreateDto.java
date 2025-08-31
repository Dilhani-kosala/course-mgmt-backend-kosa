package lk.mgmt.coursemgmtbackend.modules.terms.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class TermCreateDto {
    // getters/setters
    @NotBlank @Size(max = 32)
    private String code;

    @NotBlank @Size(max = 120)
    private String name;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

}
