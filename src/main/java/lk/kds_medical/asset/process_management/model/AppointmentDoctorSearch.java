package lk.kds_medical.asset.process_management.model;

import lk.kds_medical.asset.doctor.entity.Doctor;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AppointmentDoctorSearch {
  private Doctor doctor;
  private LocalDate localDate;
}
