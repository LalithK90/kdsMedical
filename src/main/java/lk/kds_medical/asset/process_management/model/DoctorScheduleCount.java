package lk.kds_medical.asset.process_management.model;

import lk.kds_medical.asset.doctor_schedule.entity.DoctorSchedule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorScheduleCount {
  private DoctorSchedule doctorSchedule;
  private LocalDate appointmentDate;
  private long appointmentCount;
  private long appointmentBookedCount;
  private long appointmentPaidCount;
  private long appointmentCancelCount;
  private BigDecimal totalIncome;
}
