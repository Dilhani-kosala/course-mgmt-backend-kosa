package lk.mgmt.coursemgmtbackend.modules.terms.repo;

import lk.mgmt.coursemgmtbackend.modules.terms.entity.TermEntity;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface TermRepository extends JpaRepository<TermEntity, Long> {

    boolean existsByCodeIgnoreCase(String code);
    Optional<TermEntity> findByCodeIgnoreCase(String code);

    @Query("""
    select t from TermEntity t
    where (:q is null or lower(t.code) like lower(concat('%',:q,'%'))
                 or lower(t.name) like lower(concat('%',:q,'%')))
      and (:from is null or t.startDate >= :from)
      and (:to   is null or t.endDate   <= :to)
    """)
    Page<TermEntity> search(@Param("q") String q,
                            @Param("from") LocalDate from,
                            @Param("to") LocalDate to,
                            Pageable pageable);

    // Overlap check (optional business rule)
    @Query("""
    select count(t) from TermEntity t
    where t.startDate <= :end and t.endDate >= :start
      and (:excludeId is null or t.id <> :excludeId)
    """)
    long countOverlapping(@Param("start") LocalDate start,
                          @Param("end") LocalDate end,
                          @Param("excludeId") Long excludeId);
}
