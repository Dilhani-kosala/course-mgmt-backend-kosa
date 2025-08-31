package lk.mgmt.coursemgmtbackend.modules.terms.dto;

import lk.mgmt.coursemgmtbackend.common.enums.TermStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
public class TermDto {
    // getters/setters
    private Long id;
    private String code;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private TermStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
