package com.gustas.videogamestore.dto.request;

import com.gustas.videogamestore.domain.PaymentMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CheckoutRequestDto {

    @NotNull(message = "Shipping address is required")
    private String shippingAddress;

    @NotNull(message = "Buyers name is required")
    private String buyerName;

    @NotBlank(message = "Email needs to be provided")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = "Invalid email format")
    @Schema(example = "Gustas@gmail.com")
    private String buyerEmail;

    @NotBlank(message = "Phone number needs to be provided")
    private String buyerPhone;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    @NotEmpty(message = "Items list cannot be empty")
    @Valid
    private List<CheckoutItemDto> items;

}
