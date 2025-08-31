package lk.mgmt.coursemgmtbackend.modules.offerings.repo;

import lk.mgmt.coursemgmtbackend.modules.offerings.entity.OfferingScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfferingScheduleRepository extends JpaRepository<OfferingScheduleEntity, Long> {
    void deleteByOffering_Id(Long offeringId);
}
