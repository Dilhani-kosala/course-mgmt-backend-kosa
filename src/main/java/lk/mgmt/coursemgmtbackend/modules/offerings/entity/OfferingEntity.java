package lk.mgmt.coursemgmtbackend.modules.offerings.entity;

import jakarta.persistence.*;
import lk.mgmt.coursemgmtbackend.common.enums.OfferingStatus;
import lk.mgmt.coursemgmtbackend.modules.courses.entity.CourseEntity;
import lk.mgmt.coursemgmtbackend.modules.terms.entity.TermEntity;
import lk.mgmt.coursemgmtbackend.modules.users.entity.UserEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Entity
@Table(
        name = "offerings",
        uniqueConstraints = @UniqueConstraint(name = "uq_offering_term_course_section",
                columnNames = {"term_id","course_id","section"})
)
@NamedEntityGraph(
        name = "offering.graph",
        attributeNodes = {
                @NamedAttributeNode("course"),
                @NamedAttributeNode("term"),
                @NamedAttributeNode("instructor"),
                @NamedAttributeNode("schedules")
        }
)
public class OfferingEntity {
    // getters/setters
    @Setter
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id", nullable = false)
    private CourseEntity course;

    @Setter
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "term_id", nullable = false)
    private TermEntity term;

    @Setter
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "instructor_id", nullable = false)
    private UserEntity instructor; // must have role INSTRUCTOR

    @Setter
    @Column(nullable = false, length = 16)
    private String section; // e.g., A, B1, L01

    @Setter
    @Column(nullable = false)
    private Integer capacity = 30;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 24)
    private OfferingStatus status = OfferingStatus.PLANNED;

    @Setter
    @OneToMany(mappedBy = "offering", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OfferingScheduleEntity> schedules = new LinkedHashSet<>();

    @Column(name = "created_at", updatable = false, insertable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

}
