package lk.kds_medical.asset.additionalService.dao;

import lk.kds_medical.asset.additionalService.entity.AdditionalService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdditionalServiceDao extends JpaRepository< AdditionalService, Integer > {
}
