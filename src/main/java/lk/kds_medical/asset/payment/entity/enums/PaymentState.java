package lk.kds_medical.asset.payment.entity.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentState {
    PAID("Paid"),
    CANCELLED("Cancelled");

    private final String invoiceState;
}
