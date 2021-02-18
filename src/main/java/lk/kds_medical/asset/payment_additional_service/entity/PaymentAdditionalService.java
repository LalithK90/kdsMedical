package lk.kds_medical.asset.payment_additional_service.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import lk.kds_medical.asset.additional_service.entity.AdditionalService;
import lk.kds_medical.asset.patient.entity.Patient;
import lk.kds_medical.asset.payment.entity.Payment;
import lk.kds_medical.util.audit.AuditEntity;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode( callSuper = true )
@JsonFilter( "PaymentAdditionalService" )
public class PaymentAdditionalService extends AuditEntity {


  @ManyToOne
  private Payment payment;

  @ManyToOne
  private AdditionalService additionalService;

  @ManyToOne
  private Patient patient;
}
