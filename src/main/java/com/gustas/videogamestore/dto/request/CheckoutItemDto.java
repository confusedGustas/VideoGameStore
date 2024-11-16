package com.gustas.videogamestore.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CheckoutItemDto {

    @NotNull(message = "bookId field is required")
    Long gameId;

    @NotNull(message = "quantity field is required")
    @Min(value = 1, message = "quantity must be at least 1")
    Integer quantity;

}