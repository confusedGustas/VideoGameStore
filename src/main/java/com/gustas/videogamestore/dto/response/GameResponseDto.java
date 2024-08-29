package com.gustas.videogamestore.dto.response;

import com.gustas.videogamestore.domain.Genre;
import com.gustas.videogamestore.domain.Origin;
import com.gustas.videogamestore.domain.Platform;
import com.gustas.videogamestore.domain.Publisher;
import com.gustas.videogamestore.domain.Region;
import com.gustas.videogamestore.domain.Type;
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
    private String image;
    private Integer releaseYear;
    private BigDecimal stock;
    private Region activationRegion;
    private Type activationType;
    private Platform activationPlatform;
    private Publisher publisher;
    private Origin origin;
    private Genre genre;
    private String seller;

}
