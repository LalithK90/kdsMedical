package lk.kds_medical.asset.process_management.model;

import lk.kds_medical.asset.doctor.entity.Doctor;
import lombok.Data;
import java.time.DayOfWeek;
import java.util.List;

@Data
public class AppointmentBook {
  private DayOfWeek dayOfWeek;
  private List< Doctor > doctors;

}
