package lk.mgmt.coursemgmtbackend.modules.terms.entity;

import jakarta.persistence.*;
import lk.mgmt.coursemgmtbackend.common.enums.TermStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "terms",
        uniqueConstraints = @UniqueConstraint(name = "uq_term_code", columnNames = "code"))
public class TermEntity {

    // getters/setters
    @Setter
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false, length = 32)
    private String code; // e.g., 2025-SPR

    @Setter
    @Column(nullable = false, length = 120)
    private String name; // e.g., "Spring 2025"

    @Setter
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Setter
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 24)
    private TermStatus status = TermStatus.PLANNED;

    // DB-maintained timestamps (if you added triggers/defaults)
    @Column(name = "created_at", updatable = false, insertable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

}
