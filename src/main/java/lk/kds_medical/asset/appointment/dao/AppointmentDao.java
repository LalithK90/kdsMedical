package lk.kds_medical.asset.appointment.dao;

import lk.kds_medical.asset.appointment.entity.Appointment;
import lk.kds_medical.asset.consultation.entity.Consultation;
import lk.kds_medical.asset.doctor_schedule.entity.DoctorSchedule;
import lk.kds_medical.asset.patient.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentDao extends JpaRepository< Appointment, Integer> {
  Appointment findFirstByOrderByIdDesc();

  List< Appointment> findByDateAndDoctorSchedule(LocalDate date, DoctorSchedule doctorSchedule);

  Appointment findByDoctorScheduleAndPatientAndDate(DoctorSchedule doctorSchedule, Patient patient, LocalDate date);
}
