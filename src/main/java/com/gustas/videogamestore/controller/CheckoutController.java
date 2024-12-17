package com.gustas.videogamestore.controller;

import com.gustas.videogamestore.dto.request.CheckoutRequestDto;
import com.gustas.videogamestore.dto.response.CheckoutResponseDto;
import com.gustas.videogamestore.service.checkout.CheckoutService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("api/checkout")
@AllArgsConstructor
@Tag(name = "Checkout Controllers")
public class CheckoutController {

    private CheckoutService checkoutService;

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveCheckout(@Valid @RequestBody CheckoutRequestDto checkoutRequestDto) {
        checkoutService.saveCheckout(checkoutRequestDto);
    }

    @GetMapping("/get-purchase-history")
    @ResponseStatus(HttpStatus.OK)
    public List<CheckoutResponseDto> getPurchaseHistory() {
        return checkoutService.getPurchaseHistory();
    }

}
