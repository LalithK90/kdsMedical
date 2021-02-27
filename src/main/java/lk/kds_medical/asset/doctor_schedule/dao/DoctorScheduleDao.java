package lk.kds_medical.asset.doctor_schedule.dao;

import lk.kds_medical.asset.appointment.entity.Appointment;
import lk.kds_medical.asset.doctor.entity.Doctor;
import lk.kds_medical.asset.doctor_schedule.entity.DoctorSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorScheduleDao extends JpaRepository< DoctorSchedule, Integer> {

  List< DoctorSchedule> findByDoctor(Doctor doctor);
}
