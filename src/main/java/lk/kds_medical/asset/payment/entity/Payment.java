package lk.kds_medical.asset.payment.entity;


import com.fasterxml.jackson.annotation.JsonFilter;
import lk.kds_medical.asset.common_asset.model.Enum.LiveDead;
import lk.kds_medical.asset.discount_ratio.entity.DiscountRatio;
import lk.kds_medical.asset.patient.entity.Patient;
import lk.kds_medical.asset.payment.entity.enums.InvoicePrintOrNot;
import lk.kds_medical.asset.payment.entity.enums.InvoiceValidOrNot;
import lk.kds_medical.asset.payment.entity.enums.PaymentMethod;
import lk.kds_medical.asset.payment_appointment.entity.PaymentAppointment;
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
  private BigDecimal balance;
  private InvoicePrintOrNot invoicePrintOrNot;

  @Enumerated( EnumType.STRING)
  private PaymentMethod paymentMethod;

  @Enumerated(EnumType.STRING)
  private InvoiceValidOrNot invoiceValidOrNot;

  @Enumerated(EnumType.STRING)
  private LiveDead liveDead;

  @ManyToOne
  private Patient patient;

  @ManyToOne
  private DiscountRatio discountRatio;

  @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "payment")
  private List< PaymentAppointment > PaymentAppointments;
}