package com.gustas.videogamestore.dao.checkout;

import com.gustas.videogamestore.domain.Checkout;
import com.gustas.videogamestore.repository.CheckoutRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CheckoutDaoImpl implements CheckoutDao {

    private CheckoutRepository checkoutRepository;

    @Override
    public void saveCheckout(Checkout checkout) {
        checkoutRepository.save(checkout);
    }

}
