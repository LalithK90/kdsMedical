package lk.kds_medical.asset.payment_additional_service.dao;


import lk.kds_medical.asset.payment_additional_service.entity.PaymentAdditionalService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PaymentAdditionalServiceDao extends JpaRepository< PaymentAdditionalService,Integer>{

  PaymentAdditionalService findFirstByOrderByIdDesc();

  List< PaymentAdditionalService> findByCreatedAtIsBetweenAndCreatedBy(LocalDateTime startDateTime, LocalDateTime endDateTime, String username);

  List< PaymentAdditionalService> findByCreatedAtIsBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);
}
