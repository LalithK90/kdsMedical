package lk.kds_medical.asset.process_management.model;

import lk.kds_medical.asset.doctor.entity.Doctor;
import lk.kds_medical.asset.doctor_schedule.entity.DoctorSchedule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorCount {
  private Doctor doctor;
  private long appointmentCount;
  private long appointmentPaidCount;
  private long appointmentBookedCount;
  private long appointmentCancelCount;
  private BigDecimal totalIncome;
}
