package lk.mgmt.coursemgmtbackend.modules.users.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "roles")
public class RoleEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 32)
    private String code; // ADMIN, INSTRUCTOR, STUDENT

    @Column(nullable = false, length = 64)
    private String name;

}
