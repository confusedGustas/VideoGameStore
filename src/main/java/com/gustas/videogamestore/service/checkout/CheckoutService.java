package com.gustas.videogamestore.service.checkout;

import com.gustas.videogamestore.dto.request.CheckoutRequestDto;
import org.springframework.stereotype.Service;

@Service
public interface CheckoutService {

    void saveCheckout(CheckoutRequestDto checkoutRequestDto);

}
