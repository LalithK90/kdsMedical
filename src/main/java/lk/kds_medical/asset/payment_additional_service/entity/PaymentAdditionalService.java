package lk.kds_medical.asset.payment_additional_service.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import lk.kds_medical.asset.additional_service.entity.AdditionalService;
import lk.kds_medical.asset.common_asset.model.Enum.LiveDead;
import lk.kds_medical.asset.doctor.entity.Doctor;
import lk.kds_medical.asset.patient.entity.Patient;
import lk.kds_medical.asset.payment.entity.Payment;
import lk.kds_medical.util.audit.AuditEntity;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode( callSuper = true )
@JsonFilter( "PaymentAdditionalService" )
public class PaymentAdditionalService extends AuditEntity {
  @Column( unique = true )
  private String code;

  @Enumerated( EnumType.STRING )
  private LiveDead liveDead;

  @ManyToOne
  private AdditionalService additionalService;

  @ManyToOne
  private Patient patient;

  @ManyToOne
  private Doctor doctor;

  @OneToMany( mappedBy = "paymentAdditionalService" )
  private List< Payment > payments;
}
