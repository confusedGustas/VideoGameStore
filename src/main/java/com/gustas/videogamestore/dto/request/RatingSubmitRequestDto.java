package com.gustas.videogamestore.dto.request;

import jakarta.validation.constraints.Max;
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
public class RatingSubmitRequestDto {

    @NotNull(message = "Game ID is required")
    private Long gameId;

    @NotNull(message = "Rating value is required")
    @Min(value = 1, message = "Rating value must be 1 or higher")
    @Max(value = 5, message = "Rating value must be 5 or lower")
    private Integer value;

}
