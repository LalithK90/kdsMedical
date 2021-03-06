package lk.kds_medical.asset.appointment.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import lk.kds_medical.asset.appointment.entity.enums.AppointmentStatus;
import lk.kds_medical.asset.common_asset.model.Enum.LiveDead;
import lk.kds_medical.asset.doctor_schedule.entity.DoctorSchedule;
import lk.kds_medical.asset.patient.entity.Patient;
import lk.kds_medical.asset.payment.entity.Payment;
import lk.kds_medical.util.audit.AuditEntity;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode( callSuper = true )
@ToString
@JsonFilter( "Appointment" )
public class Appointment extends AuditEntity {

  @Column( unique = true )
  private String code;

  private int number;

  @DateTimeFormat( pattern = "yyyy-MM-dd" )
  private LocalDate date;

  @Enumerated( EnumType.STRING )
  private LiveDead liveDead;

  @Enumerated( EnumType.STRING )
  private AppointmentStatus appointmentStatus;

  @ManyToOne
  private DoctorSchedule doctorSchedule;

  @ManyToOne
  private Patient patient;

  @OneToMany( mappedBy = "appointment", cascade = {CascadeType.MERGE, CascadeType.PERSIST} )
  private List< Payment > payments;
}
