package lk.kds_medical.asset.payment.entity;


import com.fasterxml.jackson.annotation.JsonFilter;
import lk.kds_medical.asset.appointment.entity.Appointment;
import lk.kds_medical.asset.common_asset.model.Enum.LiveDead;
import lk.kds_medical.asset.discount_ratio.entity.DiscountRatio;
import lk.kds_medical.asset.patient.entity.Patient;
import lk.kds_medical.asset.payment.entity.enums.PaymentPrintOrNot;
import lk.kds_medical.asset.payment.entity.enums.PaymentValidOrNot;
import lk.kds_medical.asset.payment.entity.enums.PaymentMethod;
import lk.kds_medical.asset.payment.entity.enums.PaymentWay;
import lk.kds_medical.asset.payment_additional_service.entity.PaymentAdditionalService;
import lk.kds_medical.util.audit.AuditEntity;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode( callSuper = true )
@JsonFilter( "Payment" )
public class Payment extends AuditEntity {

  private String bankName;

  private String cardNumber;

  private String remarks;

  @Column(nullable = false, unique = true)
  private String code;

  @Column(nullable = false, precision = 10, scale = 2)
  private BigDecimal totalPrice;

  @Column(nullable = false, precision = 10, scale = 2)
  private BigDecimal totalAmount;

  @Column(precision = 10, scale = 2)
  private BigDecimal discountAmount;

  @Column(precision = 10, scale = 2)
  private BigDecimal amountTendered;

  @Column(precision = 10, scale = 2)
  private BigDecimal consultationFee;

  @Column(precision = 10, scale = 2)
  private BigDecimal hospitalFee;

  @Column(precision = 10, scale = 2)
  private BigDecimal balance;

  @Enumerated( EnumType.STRING)
  private PaymentPrintOrNot paymentPrintOrNot;

  @Enumerated( EnumType.STRING)
  private PaymentMethod paymentMethod;

  @Enumerated( EnumType.STRING)
  private PaymentWay paymentWay;

  @Enumerated(EnumType.STRING)
  private PaymentValidOrNot paymentValidOrNot;

  @Enumerated(EnumType.STRING)
  private LiveDead liveDead;

  @ManyToOne
  private DiscountRatio discountRatio;

  @ManyToOne
  private Appointment appointment;

  @ManyToOne
  private PaymentAdditionalService paymentAdditionalService;

}
