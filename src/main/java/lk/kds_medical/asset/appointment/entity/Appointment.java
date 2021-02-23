package lk.kds_medical.asset.appointment.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import lk.kds_medical.asset.appointment.entity.enums.AppointmentStatus;
import lk.kds_medical.asset.common_asset.model.Enum.LiveDead;
import lk.kds_medical.asset.doctor_schedule.entity.DoctorSchedule;
import lk.kds_medical.asset.payment_appointment.entity.PaymentAppointment;
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
@JsonFilter( "Appointment" )
public class Appointment extends AuditEntity {

  @Column( unique = true )
  private String code;

  private String number;

  @DateTimeFormat( pattern = "yyyy-MM-dd" )
  private LocalDate date;

  @Enumerated( EnumType.STRING )
  private LiveDead liveDead;

  @Enumerated( EnumType.STRING )
  private AppointmentStatus appointmentStatus;

  @ManyToOne
  private DoctorSchedule doctorSchedule;

  @OneToMany( mappedBy = "appointment" )
  private List< PaymentAppointment > paymentAppointments;
}
