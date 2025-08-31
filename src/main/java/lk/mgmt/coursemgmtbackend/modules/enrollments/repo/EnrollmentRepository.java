package lk.mgmt.coursemgmtbackend.modules.enrollments.repo;

import lk.mgmt.coursemgmtbackend.common.enums.EnrollmentStatus;
import lk.mgmt.coursemgmtbackend.modules.enrollments.entity.EnrollmentEntity;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

public interface EnrollmentRepository extends JpaRepository<EnrollmentEntity, Long> {

    boolean existsByStudent_IdAndOffering_IdAndStatus(Long studentId, Long offeringId, EnrollmentStatus status);

    Optional<EnrollmentEntity> findByStudent_IdAndOffering_Id(Long studentId, Long offeringId);

    long countByOffering_IdAndStatus(Long offeringId, EnrollmentStatus status);

    @EntityGraph(value = "enrollment.graph")
    Page<EnrollmentEntity> findByStudent_Id(Long studentId, Pageable pageable);

    @EntityGraph(value = "enrollment.graph")
    Page<EnrollmentEntity> findByOffering_Id(Long offeringId, Pageable pageable);

    @EntityGraph(value = "enrollment.graph")
    Page<EnrollmentEntity> findByOffering_Instructor_Id(Long instructorId, Pageable pageable);

    // Active enrollments for a student in a given term (for schedule clash checks)
    @EntityGraph(value = "enrollment.graph")
    List<EnrollmentEntity> findByStudent_IdAndOffering_Term_IdAndStatus(Long studentId, Long termId, EnrollmentStatus status);

    // Count COMPLETED for a set of course IDs (prereq check)
    @Query("""
    select count(e) from EnrollmentEntity e
    where e.student.id = :studentId
      and e.status = lk.mgmt.coursemgmtbackend.common.enums.EnrollmentStatus.COMPLETED
      and e.offering.course.id in :courseIds
  """)
    long countCompletedForCourses(@Param("studentId") Long studentId, @Param("courseIds") Collection<Long> courseIds);

    @EntityGraph(attributePaths = {
            "offering","offering.course","offering.term","offering.instructor","student"
    })
    List<EnrollmentEntity> findByStudent_IdAndStatus(Long studentId, EnrollmentStatus status);

    // For instructor bulk grading (fetch their offering + enrolled students)
    @EntityGraph(attributePaths = {
            "offering","offering.course","offering.term","offering.instructor","student"
    })
    List<EnrollmentEntity> findByOffering_IdAndStatus(Long offeringId, EnrollmentStatus status);
}
