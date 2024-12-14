package com.gustas.videogamestore.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserSearchCriteria {

    private Integer limit;
    private Integer offset;
    private String search;

}
