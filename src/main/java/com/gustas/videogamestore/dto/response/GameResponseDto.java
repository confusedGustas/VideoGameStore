package com.gustas.videogamestore.dto.response;

import com.gustas.videogamestore.domain.Genre;
import com.gustas.videogamestore.domain.Platform;
import com.gustas.videogamestore.domain.Publisher;
import com.gustas.videogamestore.domain.Region;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class GameResponseDto {

    private Long id;
    private String name;
    private BigDecimal price;
    private String description;
    private Integer releaseYear;
    private Integer stock;
    private Region activationRegion;
    private Platform activationPlatform;
    private Publisher publisher;
    private Genre genre;
    private String seller;

}
