package lk.kds_medical.asset.payment.dao;

import lk.kds_medical.asset.appointment.entity.Appointment;
import lk.kds_medical.asset.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentDao extends JpaRepository<Payment,Integer>{

  Payment findFirstByOrderByIdDesc();

  Payment findByAppointment(Appointment appointment);
}
