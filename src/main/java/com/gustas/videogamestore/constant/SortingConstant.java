package com.gustas.videogamestore.constant;

import java.util.Set;

public class SortingConstant {

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PRICE = "price";

    public static final Set<String> VALID_SORT_COLUMNS = Set.of(
            COLUMN_ID,
            COLUMN_NAME,
            COLUMN_PRICE
    );

    private SortingConstant() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated.");
    }
}
