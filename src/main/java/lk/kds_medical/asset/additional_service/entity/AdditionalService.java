package lk.kds_medical.asset.additional_service.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import lk.kds_medical.asset.common_asset.model.Enum.LiveDead;
import lk.kds_medical.asset.payment_additional_service.entity.PaymentAdditionalService;
import lk.kds_medical.util.audit.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
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
  @Enumerated( EnumType.STRING )
  private LiveDead liveDead;

  private String name;

  private BigDecimal price;

  @OneToMany( mappedBy = "additionalService" )
  private List< PaymentAdditionalService > paymentAdditionalServices;
}
