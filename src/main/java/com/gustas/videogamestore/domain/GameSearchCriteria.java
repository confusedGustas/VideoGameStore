package com.gustas.videogamestore.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GameSearchCriteria {

    private Integer limit;
    private Integer offset;
    private SortOrder sortOrder;
    private String sortColumn;
    private String search;
    private Integer rating;

}
