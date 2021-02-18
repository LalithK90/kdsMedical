package lk.kds_medical.asset.additional_service.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import lk.kds_medical.asset.payment_additional_service.entity.PaymentAdditionalService;
import lk.kds_medical.util.audit.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter( "AdditionalService" )
public class AdditionalService extends AuditEntity {

  private String name;

  private BigDecimal price;

  @OneToMany( mappedBy = "additionalService" )
  private List< PaymentAdditionalService > paymentAdditionalServices;
}
