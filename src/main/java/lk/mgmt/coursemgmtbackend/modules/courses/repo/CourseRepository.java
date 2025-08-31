package lk.mgmt.coursemgmtbackend.modules.courses.repo;

import lk.mgmt.coursemgmtbackend.modules.courses.entity.CourseEntity;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<CourseEntity, Long> {

    boolean existsByCodeIgnoreCase(String code);
    Optional<CourseEntity> findByCodeIgnoreCase(String code);

    @EntityGraph(value = "course.withDeptAndPrereqs")
    @Query("""
      select c from CourseEntity c
      where (:q is null or lower(c.code) like lower(concat('%',:q,'%'))
                     or lower(c.title) like lower(concat('%',:q,'%')))
        and (:deptId is null or c.department.id = :deptId)
      """)
    Page<CourseEntity> search(String q, Long deptId, Pageable pageable);

    @EntityGraph(value = "course.withDeptAndPrereqs")
    Optional<CourseEntity> findWithDeptAndPrereqsById(Long id);
}
