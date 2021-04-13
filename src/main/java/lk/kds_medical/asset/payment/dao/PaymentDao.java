package lk.kds_medical.asset.payment.dao;

import lk.kds_medical.asset.appointment.entity.Appointment;
import lk.kds_medical.asset.payment.entity.Payment;
import lk.kds_medical.asset.payment_additional_service.entity.PaymentAdditionalService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PaymentDao extends JpaRepository<Payment,Integer>{

  Payment findFirstByOrderByIdDesc();

  Payment findByAppointment(Appointment appointment);

  Payment findByPaymentAdditionalService(PaymentAdditionalService paymentAdditionalService);

  List< Payment> findByCreatedAtIsBetweenAndCreatedBy(LocalDateTime startDateTime, LocalDateTime endDateTime, String username);

  List< Payment> findByCreatedAtIsBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);
}
