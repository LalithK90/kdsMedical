package lk.kds_medical.asset.payment.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentWay {
ADD("Other Service"),
  DOC("Doctor Appointment");

private final String paymentWay;
}
