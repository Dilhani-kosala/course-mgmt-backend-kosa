package lk.mgmt.coursemgmtbackend.modules.terms.dto;

import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lk.mgmt.coursemgmtbackend.common.enums.TermStatus;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TermUpdateDto {
    // getters/setters
    @Size(max = 32)  private String code;
    @Size(max = 120) private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private TermStatus status;

}
