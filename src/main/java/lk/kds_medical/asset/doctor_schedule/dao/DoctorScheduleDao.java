package lk.kds_medical.asset.doctor_schedule.dao;

import lk.kds_medical.asset.appointment.entity.Appointment;
import lk.kds_medical.asset.doctor_schedule.entity.DoctorSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorScheduleDao extends JpaRepository< DoctorSchedule, Integer> {

}
