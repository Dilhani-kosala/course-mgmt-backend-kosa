package lk.mgmt.coursemgmtbackend.modules.offerings.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Setter
@Getter
@Entity
@Table(
        name = "offering_schedules",
        uniqueConstraints = @UniqueConstraint(name = "uq_offering_day_time",
                columnNames = {"offering_id","day_of_week","start_time","end_time"})
)
public class OfferingScheduleEntity {
    // getters/setters
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "offering_id", nullable = false)
    private OfferingEntity offering;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false, length = 10)
    private DayOfWeek dayOfWeek;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(length = 64)
    private String location; // room / lab

}
