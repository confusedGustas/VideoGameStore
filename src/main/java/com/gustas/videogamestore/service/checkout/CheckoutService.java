package com.gustas.videogamestore.service.checkout;

import com.gustas.videogamestore.dto.request.CheckoutRequestDto;
import com.gustas.videogamestore.dto.response.CheckoutResponseDto;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface CheckoutService {

    void saveCheckout(CheckoutRequestDto checkoutRequestDto);
    List<CheckoutResponseDto> getPurchaseHistory();

}
