package lk.kds_medical.asset.payment_appointment.dao;

import lk.kds_medical.asset.payment_appointment.entity.PaymentAppointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentAppointmentDao extends JpaRepository<PaymentAppointment,Integer>{

  PaymentAppointment findFirstByOrderByIdDesc();
}
