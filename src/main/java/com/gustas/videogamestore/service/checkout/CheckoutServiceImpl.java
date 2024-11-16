package com.gustas.videogamestore.service.checkout;

import com.gustas.videogamestore.dao.checkout.CheckoutDao;
import com.gustas.videogamestore.dao.game.GameDao;
import com.gustas.videogamestore.domain.Checkout;
import com.gustas.videogamestore.domain.CheckoutItem;
import com.gustas.videogamestore.domain.Game;
import com.gustas.videogamestore.dto.request.CheckoutRequestDto;
import com.gustas.videogamestore.mapper.CheckoutMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class CheckoutServiceImpl implements CheckoutService {

    private CheckoutDao checkoutDao;
    private CheckoutMapper checkoutMapper;
    private GameDao gameDao;

    @Override
    public void saveCheckout(CheckoutRequestDto checkoutRequestDto) {
        Checkout checkout = checkoutMapper.toEntity(checkoutRequestDto);

        for (CheckoutItem item : checkout.getItems()) {
            Game game = gameDao.getGame(item.getGame().getId());

            if (game.getStock() < item.getQuantity()) {
                throw new IllegalArgumentException("Not enough stock for game with id: " + game.getId());
            }

            BigDecimal totalPrice = game.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            game.setStock(game.getStock() - item.getQuantity());
            gameDao.saveGame(game);

            checkout.setTotalPrice(checkout.getTotalPrice().add(totalPrice));
        }

        checkoutDao.saveCheckout(checkout);
    }

}
