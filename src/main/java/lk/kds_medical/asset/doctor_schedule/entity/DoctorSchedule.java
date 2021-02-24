package lk.kds_medical.asset.doctor_schedule.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import lk.kds_medical.asset.appointment.entity.Appointment;
import lk.kds_medical.asset.common_asset.model.Enum.LiveDead;
import lk.kds_medical.asset.doctor.entity.Doctor;
import lk.kds_medical.util.audit.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

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

  private boolean active;

  @Enumerated(EnumType.STRING)
  private LiveDead liveDead;

  @ManyToOne
  private Doctor doctor;

  @OneToMany(mappedBy = "doctorSchedule")
  private List< Appointment > appointments;

  @Transient
  private int count;

  @Transient
  private LocalDate appointmentDate;
}
