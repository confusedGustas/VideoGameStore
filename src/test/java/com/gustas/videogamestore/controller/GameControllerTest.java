package com.gustas.videogamestore.controller;

import com.gustas.videogamestore.AbstractIntegrationTest;
import org.json.JSONException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static com.gustas.videogamestore.constants.TestConstants.GET_GAMES_URL;
import static com.gustas.videogamestore.constants.TestConstants.LIMIT_ATTRIBUTE;
import static com.gustas.videogamestore.constants.TestConstants.OFFSET_ATTRIBUTE;
import static com.gustas.videogamestore.constants.TestConstants.SORT_COLUMN_ATTRIBUTE;
import static com.gustas.videogamestore.constants.TestConstants.SORT_ORDER_ATTRIBUTE;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GameControllerTest extends AbstractIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    static Stream<Arguments> gameResponseProvider() {
        return Stream.of(
                Arguments.of(null, null, null, null, "src/test/resources/validGetGamesResponseJsonExample.json"),
                Arguments.of("30", "2", null, null, "src/test/resources/validGetGamesResponseLimit3Offset2JsonExample.json"),
                Arguments.of("40", "7", null, null, "src/test/resources/validGetGamesResponseLimit4Offset7JsonExample.json"),
                Arguments.of("10", "10", null, null, "src/test/resources/validGetGamesResponseLimit10Offset10JsonExample.json"),
                Arguments.of("20", "120", "ASC", "price", "src/test/resources/validGetGamesResponseLimit2Offset12OrderASCColumnPriceJsonExample.json"),
                Arguments.of("40", "10", "DESC", "name", "src/test/resources/validGetGamesResponseLimit4Offset1OrderDESCColumnNameJsonExample.json"),
                Arguments.of(null, null, "ASC", "price", "src/test/resources/validGetGamesResponseOrderASCColumnPriceJsonExample.json"),
                Arguments.of(null, null, "DESC", "name", "src/test/resources/validGetGamesResponseOrderDESCColumnNameJsonExample.json"),
                Arguments.of(null, null, "DESC", "price", "src/test/resources/validGetGamesResponseOrderDESCColumnPriceJsonExample.json"),
                Arguments.of(null, null, "ASC", "name", "src/test/resources/validGetGamesResponseOrderASCColumnNameJsonExample.json")
        );
    }

    @ParameterizedTest
    @MethodSource("gameResponseProvider")
    void testGetGamesResponses(String limit, String offset, String sortOrder, String sortColumn, String expectedJsonFilePath) throws IOException, JSONException {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(GET_GAMES_URL);

        if (limit != null) {
            builder.queryParam(LIMIT_ATTRIBUTE, limit);
        }
        if (offset != null) {
            builder.queryParam(OFFSET_ATTRIBUTE, offset);
        }
        if (sortOrder != null) {
            builder.queryParam(SORT_ORDER_ATTRIBUTE, sortOrder);
        }
        if (sortColumn != null) {
            builder.queryParam(SORT_COLUMN_ATTRIBUTE, sortColumn);
        }

        String url = builder.toUriString();

        ResponseEntity<String> response = testRestTemplate.getForEntity(url, String.class);
        String expectedJson = Files.readString(Path.of(expectedJsonFilePath));
        JSONAssert.assertEquals(expectedJson, response.getBody(), true);
    }
}
