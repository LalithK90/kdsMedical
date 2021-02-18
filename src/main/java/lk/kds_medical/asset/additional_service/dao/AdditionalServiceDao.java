package lk.kds_medical.asset.additional_service.dao;

import lk.kds_medical.asset.additional_service.entity.AdditionalService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdditionalServiceDao extends JpaRepository< AdditionalService, Integer > {
}
