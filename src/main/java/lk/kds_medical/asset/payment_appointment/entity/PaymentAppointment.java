package lk.kds_medical.asset.payment_appointment.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import lk.kds_medical.asset.appointment.entity.Appointment;
import lk.kds_medical.asset.common_asset.model.Enum.LiveDead;
import lk.kds_medical.asset.patient.entity.Patient;
import lk.kds_medical.asset.payment.entity.Payment;
import lk.kds_medical.util.audit.AuditEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode( callSuper = true )
@JsonFilter( "PaymentAppointment" )
public class PaymentAppointment extends AuditEntity {

  @ManyToOne
  private Payment payment;

  @ManyToOne
  private Appointment appointment;

  @ManyToOne
  private Patient patient;

  @Enumerated( EnumType.STRING )
  private LiveDead liveDead;

}
