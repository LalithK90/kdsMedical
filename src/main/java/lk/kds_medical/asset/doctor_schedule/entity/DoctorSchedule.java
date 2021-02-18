package lk.kds_medical.asset.doctor_schedule.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import lk.kds_medical.asset.doctor.entity.Doctor;
import lk.kds_medical.util.audit.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter( "DoctorSchedule" )
public class DoctorSchedule extends AuditEntity {

  @Enumerated( EnumType.STRING)
  private DayOfWeek dayOfWeek;

  @DateTimeFormat( pattern = "HH:MM" )
  private LocalTime arrivalTime;

  private boolean active = false;

  @ManyToOne
  private Doctor doctor;
}
