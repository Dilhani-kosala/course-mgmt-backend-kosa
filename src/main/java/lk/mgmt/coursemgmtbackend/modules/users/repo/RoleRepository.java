package lk.mgmt.coursemgmtbackend.modules.users.repo;

import lk.mgmt.coursemgmtbackend.modules.users.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByCode(String code);
    boolean existsByCode(String code);
}
