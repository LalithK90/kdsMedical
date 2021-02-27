package lk.kds_medical.asset.payment.entity.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentPrintOrNot {
  PRINTED("Want"),
  NOT_PRINTED("No Want");
  private final String invoicePrintOrNot;
}
