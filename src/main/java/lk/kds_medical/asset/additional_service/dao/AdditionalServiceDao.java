package lk.kds_medical.asset.additional_service.dao;

import lk.kds_medical.asset.additional_service.entity.AdditionalService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AdditionalServiceDao extends JpaRepository< AdditionalService, Integer > {
  List< AdditionalService> findByCreatedAtIsBetweenAndCreatedBy(LocalDateTime startDate, LocalDateTime endDate, String username);

  List< AdditionalService> findByCreatedAtIsBetween(LocalDateTime startDate, LocalDateTime endDate);
}
