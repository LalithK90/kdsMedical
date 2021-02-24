package lk.kds_medical.asset.process_management.model;

import lk.kds_medical.asset.doctor.entity.Doctor;
import lk.kds_medical.asset.doctor_schedule.entity.DoctorSchedule;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class AppointmentDoctorSearch {
  private Doctor doctor;
  private LocalDate localDate;
  private DoctorSchedule doctorSchedule;
}
