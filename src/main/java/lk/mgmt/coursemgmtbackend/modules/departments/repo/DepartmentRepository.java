package lk.mgmt.coursemgmtbackend.modules.departments.repo;

import lk.mgmt.coursemgmtbackend.modules.departments.entity.DepartmentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<DepartmentEntity, Long> {
    boolean existsByCodeIgnoreCase(String code);
    Optional<DepartmentEntity> findByCodeIgnoreCase(String code);

    Page<DepartmentEntity> findByCodeContainingIgnoreCaseOrNameContainingIgnoreCase(
            String code, String name, Pageable pageable);
}
