package com.gustas.videogamestore.mapper;

import com.gustas.videogamestore.dao.game.GameDao;
import com.gustas.videogamestore.domain.Checkout;
import com.gustas.videogamestore.domain.CheckoutItem;
import com.gustas.videogamestore.dto.request.CheckoutRequestDto;
import com.gustas.videogamestore.dto.response.CheckoutResponseDto;
import com.gustas.videogamestore.service.session.SessionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class CheckoutMapper {

    private GameDao gameDao;
    private SessionService sessionService;

    public Checkout toEntity(CheckoutRequestDto checkoutRequestDto) {
        Checkout checkout = new Checkout();

        checkout.setUser(sessionService.getUserFromSessionId());
        checkout.setShippingAddress(checkoutRequestDto.getShippingAddress());
        checkout.setOrderDate(LocalDateTime.now());
        checkout.setBuyerName(checkoutRequestDto.getBuyerName());
        checkout.setBuyerEmail(checkoutRequestDto.getBuyerEmail());
        checkout.setBuyerPhone(checkoutRequestDto.getBuyerPhone());
        checkout.setPaymentMethod(checkoutRequestDto.getPaymentMethod());
        checkout.setTotalPrice(BigDecimal.valueOf(0));

        List<CheckoutItem> items = checkoutRequestDto.getItems().stream()
                .map(itemDto -> {
                    CheckoutItem item = new CheckoutItem();
                    item.setGame(gameDao.getGame(itemDto.getGameId()));
                    item.setQuantity(itemDto.getQuantity());
                    item.setCheckout(checkout);
                    return item;
                })
                .collect(Collectors.toList());
        checkout.setItems(items);

        return checkout;
    }

    public List<CheckoutResponseDto> checkoutResponseDto(List<Checkout> checkouts) {
        return checkouts.stream()
                .map(checkout -> {
                    CheckoutResponseDto checkoutResponseDto = new CheckoutResponseDto();
                    checkoutResponseDto.setOrderDate(checkout.getOrderDate());
                    checkoutResponseDto.setTotalAmount(checkout.getTotalPrice());
                    checkoutResponseDto.setShippingAddress(checkout.getShippingAddress());
                    checkoutResponseDto.setBuyerName(checkout.getBuyerName());
                    checkoutResponseDto.setBuyerEmail(checkout.getBuyerEmail());
                    checkoutResponseDto.setBuyerPhone(checkout.getBuyerPhone());
                    checkoutResponseDto.setPaymentMethod(checkout.getPaymentMethod());
                    return checkoutResponseDto;
                })
                .collect(Collectors.toList());
    }

}
