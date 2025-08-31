package lk.mgmt.coursemgmtbackend.modules.courses.repo;

import lk.mgmt.coursemgmtbackend.modules.courses.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoursePrerequisiteRepository extends JpaRepository<CoursePrerequisiteEntity, CoursePrerequisiteId> {
    boolean existsByPrerequisite_Id(Long prereqCourseId);
    void deleteByCourse_Id(Long courseId);
}
