package lk.mgmt.coursemgmtbackend.modules.enrollments.entity;

import jakarta.persistence.*;
import lk.mgmt.coursemgmtbackend.common.enums.EnrollmentStatus;
import lk.mgmt.coursemgmtbackend.common.enums.Grade;
import lk.mgmt.coursemgmtbackend.modules.offerings.entity.OfferingEntity;
import lk.mgmt.coursemgmtbackend.modules.users.entity.UserEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "enrollments",
        uniqueConstraints = @UniqueConstraint(name = "uq_enrollment_student_offering",
                columnNames = {"student_id","offering_id"}))
@NamedEntityGraph(
        name = "enrollment.graph",
        attributeNodes = {
                @NamedAttributeNode(value = "offering", subgraph = "offeringSub"),
                @NamedAttributeNode(value = "student")
        },
        subgraphs = {
                @NamedSubgraph(name = "offeringSub", attributeNodes = {
                        @NamedAttributeNode("course"),
                        @NamedAttributeNode("term"),
                        @NamedAttributeNode("schedules"),
                        @NamedAttributeNode("instructor")
                })
        }
)
public class EnrollmentEntity {
    // getters/setters
    @Setter
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "offering_id", nullable = false)
    private OfferingEntity offering;

    @Setter
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id", nullable = false)
    private UserEntity student; // role = STUDENT

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 24)
    private EnrollmentStatus status = EnrollmentStatus.ENROLLED;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(length = 8)
    private Grade grade; // optional; set when COMPLETED

    @Column(name = "enrolled_at", updatable = false, insertable = false)
    private LocalDateTime enrolledAt;

}
