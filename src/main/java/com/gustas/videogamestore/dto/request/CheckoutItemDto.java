package com.gustas.videogamestore.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CheckoutItemDto {

    @NotNull(message = "bookId field is required")
    private Long gameId;

    @NotNull(message = "quantity field is required")
    @Min(value = 1, message = "quantity must be at least 1")
    private Integer quantity;

}