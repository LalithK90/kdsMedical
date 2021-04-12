package lk.kds_medical.asset.payment.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentValidOrNot {
    VALID("Valid"),
    NOT_PRINTED("No Valid");
    private final String invoiceValidOrNot;
}
