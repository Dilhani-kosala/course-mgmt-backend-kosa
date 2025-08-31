package lk.mgmt.coursemgmtbackend.modules.courses.entity;

import jakarta.persistence.*;
import lk.mgmt.coursemgmtbackend.modules.departments.entity.DepartmentEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "courses",
        uniqueConstraints = @UniqueConstraint(name = "uq_course_code", columnNames = "code"))
@NamedEntityGraph(
        name = "course.withDeptAndPrereqs",
        attributeNodes = {
                @NamedAttributeNode("department"),
                @NamedAttributeNode(value = "prerequisites", subgraph = "prereqSub")
        },
        subgraphs = @NamedSubgraph(
                name = "prereqSub",
                attributeNodes = @NamedAttributeNode("prerequisite")
        )
)
public class CourseEntity {
    // getters/setters
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 32)
    private String code;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(length = 2000)
    private String description;

    @Column(nullable = false)
    private Double credits;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "department_id", nullable = false)
    private DepartmentEntity department;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CoursePrerequisiteEntity> prerequisites = new LinkedHashSet<>();

}
