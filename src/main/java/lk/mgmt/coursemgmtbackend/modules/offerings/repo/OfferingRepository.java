package lk.mgmt.coursemgmtbackend.modules.offerings.repo;

import lk.mgmt.coursemgmtbackend.modules.offerings.entity.OfferingEntity;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;

import java.util.Optional;

public interface OfferingRepository extends JpaRepository<OfferingEntity, Long> {

    @EntityGraph(value = "offering.graph")
    @Query("""
    select o from OfferingEntity o
    where (:q is null or lower(o.course.code) like lower(concat('%',:q,'%'))
                   or lower(o.course.title) like lower(concat('%',:q,'%'))
                   or lower(o.section) like lower(concat('%',:q,'%')))
      and (:termId is null or o.term.id = :termId)
      and (:courseId is null or o.course.id = :courseId)
      and (:instructorId is null or o.instructor.id = :instructorId)
  """)
    Page<OfferingEntity> search(String q, Long termId, Long courseId, Long instructorId, Pageable pageable);

    @EntityGraph(value = "offering.graph")
    Optional<OfferingEntity> findWithGraphById(Long id);

    boolean existsByTerm_IdAndCourse_IdAndSectionIgnoreCase(Long termId, Long courseId, String section);
}
