package lk.kds_medical.asset.appointment.dao;

import lk.kds_medical.asset.appointment.entity.Appointment;
import lk.kds_medical.asset.consultation.entity.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentDao extends JpaRepository< Appointment, Integer> {

}
