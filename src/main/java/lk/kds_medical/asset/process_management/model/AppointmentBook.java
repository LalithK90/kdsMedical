package lk.kds_medical.asset.process_management.model;

import lk.kds_medical.asset.doctor.entity.Doctor;
import lk.kds_medical.asset.doctor_schedule.entity.DoctorSchedule;
import lombok.Data;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class AppointmentBook {
  private DayOfWeek dayOfWeek;
  private List< DoctorSchedule > doctorSchedules;
  private LocalDate appointmentDate;
}
