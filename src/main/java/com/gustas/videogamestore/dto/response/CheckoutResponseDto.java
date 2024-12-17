package com.gustas.videogamestore.dto.response;

import com.gustas.videogamestore.domain.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CheckoutResponseDto {

    private String shippingAddress;
    private LocalDateTime orderDate;
    private String buyerName;
    private String buyerEmail;
    private String buyerPhone;
    private PaymentMethod paymentMethod;
    private BigDecimal totalAmount;

}
