package lk.mgmt.coursemgmtbackend.modules.courses.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Setter
@Getter
@Embeddable
public class CoursePrerequisiteId implements Serializable {
    private Long courseId;
    private Long prereqCourseId;

    public CoursePrerequisiteId() {}
    public CoursePrerequisiteId(Long courseId, Long prereqCourseId) {
        this.courseId = courseId; this.prereqCourseId = prereqCourseId;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CoursePrerequisiteId that)) return false;
        return Objects.equals(courseId, that.courseId) &&
                Objects.equals(prereqCourseId, that.prereqCourseId);
    }
    @Override public int hashCode() { return Objects.hash(courseId, prereqCourseId); }
}
