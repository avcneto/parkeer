package com.parkeer.parkeer.entity.payment_method;


import com.parkeer.parkeer.dto.payment_method.PaymentMethodDTO;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.GeneratedValue;

import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "tb_payment_method")
public class PaymentMethod {

    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private Long userId;
    private PaymentMethodType paymentMethodType;
    private String pixKey;
    private String cardNumber;
    private String cardFlag;

    public PaymentMethod(PaymentMethodDTO paymentMethodDTO) {
        this.userId = paymentMethodDTO.userId();
        this.paymentMethodType = paymentMethodDTO.paymentMethodType();
        this.pixKey = paymentMethodDTO.pixKey();
        this.cardNumber = paymentMethodDTO.cardNumber();
        this.cardFlag = paymentMethodDTO.cardFlag();
    }
}
