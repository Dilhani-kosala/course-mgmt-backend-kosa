package lk.mgmt.coursemgmtbackend.modules.courses.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "course_prerequisites")
public class CoursePrerequisiteEntity {

    @EmbeddedId
    private CoursePrerequisiteId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("courseId")
    @JoinColumn(name = "course_id", nullable = false)
    private CourseEntity course;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("prereqCourseId")
    @JoinColumn(name = "prereq_course_id", nullable = false)
    private CourseEntity prerequisite;

    public CoursePrerequisiteEntity() {}
    public CoursePrerequisiteEntity(CourseEntity course, CourseEntity prerequisite) {
        this.course = course;
        this.prerequisite = prerequisite;
        this.id = new CoursePrerequisiteId(course.getId(), prerequisite.getId());
    }

}
