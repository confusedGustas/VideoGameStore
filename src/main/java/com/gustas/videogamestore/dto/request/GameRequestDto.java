package com.gustas.videogamestore.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class GameRequestDto {

    @NotBlank(message = "Name is required")
    private String name;
    @NotNull @Min(value = 0, message = "Price must be greater than 0")
    private BigDecimal price;
    @NotBlank(message = "Description is required")
    private String description;
    @NotNull @Min(value = 0, message = "Release date must be greater than 0")
    private Integer releaseYear;
    @NotBlank(message = "Region is required")
    private String region;
    @NotBlank(message = "Platform is required")
    private String platform;
    @NotBlank(message = "Publisher is required")
    private String publisher;
    @NotBlank(message = "Genre is required")
    private String genre;
    @NotNull @Min(value = 0, message = "Stock must be greater than 0")
    private BigDecimal stock;

}
