package com.gustas.videogamestore.dao.checkout;

import com.gustas.videogamestore.domain.Checkout;
import java.util.List;

public interface CheckoutDao {

    void saveCheckout(Checkout checkout);
    List<Checkout> getAllCheckoutsByUserId(Long userId);

}
