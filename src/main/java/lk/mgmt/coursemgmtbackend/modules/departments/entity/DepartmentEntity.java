package lk.mgmt.coursemgmtbackend.modules.departments.entity;

import jakarta.persistence.*;
import lk.mgmt.coursemgmtbackend.common.enums.DepartmentStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "departments")
public class DepartmentEntity {

    // getters/setters
    @Setter
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false, unique = true, length = 32)
    private String code;   // e.g., CS, MATH

    @Setter
    @Column(nullable = false, length = 120)
    private String name;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 24)
    private DepartmentStatus status = DepartmentStatus.ACTIVE;

    // handled by DB defaults/triggers; mapped as read-only here
    @Column(name = "created_at", updatable = false, insertable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

}
