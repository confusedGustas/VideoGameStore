package com.gustas.videogamestore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gustas.videogamestore.AbstractIntegrationTest;
import com.gustas.videogamestore.dto.response.PaginatedResponseDto;
import org.json.JSONException;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.gustas.videogamestore.constants.TestConstants.GET_GAMES_URL;
import static com.gustas.videogamestore.constants.TestConstants.LIMIT_ATTRIBUTE;
import static com.gustas.videogamestore.constants.TestConstants.OFFSET_ATTRIBUTE;
import static com.gustas.videogamestore.constants.TestConstants.SORT_COLUMN_ATTRIBUTE;
import static com.gustas.videogamestore.constants.TestConstants.SORT_ORDER_ATTRIBUTE;
import static org.junit.Assert.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GameControllerTest extends AbstractIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testGetGamesResponse() throws IOException, JSONException {
        ResponseEntity<PaginatedResponseDto> getResponse = testRestTemplate.getForEntity(
                GET_GAMES_URL,
                PaginatedResponseDto.class
        );

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());

        String responseToJson = objectMapper.writeValueAsString(getResponse.getBody());
        Path expectedJsonFilePath = Path.of("src/test/resources/validGetGamesResponseJsonExample.json");
        String expectedJson = Files.readString(expectedJsonFilePath);

        JSONAssert.assertEquals(expectedJson, responseToJson, true);
    }

    @Test
    public void testPaginationLimit3WithOffset2Response() throws IOException, JSONException {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(GET_GAMES_URL)
                .queryParam(LIMIT_ATTRIBUTE, "3")
                .queryParam(OFFSET_ATTRIBUTE, "2");

        String url = builder.toUriString();

        ResponseEntity<String> response = testRestTemplate.getForEntity(url, String.class);

        Path expectedJsonFilePath = Path.of("src/test/resources/validGetGamesResponseLimit3Offset2JsonExample.json");
        String expectedJson = Files.readString(expectedJsonFilePath);

        JSONAssert.assertEquals(expectedJson, response.getBody(), true);
    }

    @Test
    public void testPaginationLimit4WithOffset7Response() throws IOException, JSONException {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(GET_GAMES_URL)
                .queryParam(LIMIT_ATTRIBUTE, "4")
                .queryParam(OFFSET_ATTRIBUTE, "7");

        String url = builder.toUriString();

        ResponseEntity<String> response = testRestTemplate.getForEntity(url, String.class);

        Path expectedJsonFilePath = Path.of("src/test/resources/validGetGamesResponseLimit4Offset7JsonExample.json");
        String expectedJson = Files.readString(expectedJsonFilePath);

        JSONAssert.assertEquals(expectedJson, response.getBody(), true);
    }

    @Test
    public void testPaginationLimit5WithOffset0Response() throws IOException, JSONException {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(GET_GAMES_URL)
                .queryParam(LIMIT_ATTRIBUTE, "5")
                .queryParam(OFFSET_ATTRIBUTE, "0");

        String url = builder.toUriString();

        ResponseEntity<String> response = testRestTemplate.getForEntity(url, String.class);

        Path expectedJsonFilePath = Path.of("src/test/resources/validGetGamesResponseLimit5Offset0JsonExample.json");
        String expectedJson = Files.readString(expectedJsonFilePath);

        JSONAssert.assertEquals(expectedJson, response.getBody(), true);
    }

    @Test
    public void testPaginationLimit10WithOffset10Response() throws IOException, JSONException {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(GET_GAMES_URL)
                .queryParam(LIMIT_ATTRIBUTE, "10")
                .queryParam(OFFSET_ATTRIBUTE, "10");

        String url = builder.toUriString();

        ResponseEntity<String> response = testRestTemplate.getForEntity(url, String.class);

        Path expectedJsonFilePath = Path.of("src/test/resources/validGetGamesResponseLimit10Offset10JsonExample.json");
        String expectedJson = Files.readString(expectedJsonFilePath);

        JSONAssert.assertEquals(expectedJson, response.getBody(), true);
    }

    @Test
    public void testPaginationLimit2WithOffset12OrderASCColumnPriceResponse() throws IOException, JSONException {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(GET_GAMES_URL)
                .queryParam(LIMIT_ATTRIBUTE, "2")
                .queryParam(OFFSET_ATTRIBUTE, "12")
                .queryParam(SORT_ORDER_ATTRIBUTE, "ASC")
                .queryParam(SORT_COLUMN_ATTRIBUTE, "price");

        String url = builder.toUriString();

        ResponseEntity<String> response = testRestTemplate.getForEntity(url, String.class);

        Path expectedJsonFilePath = Path.of("src/test/resources/validGetGamesResponseLimit2Offset12OrderASCColumnPriceJsonExample.json");
        String expectedJson = Files.readString(expectedJsonFilePath);

        JSONAssert.assertEquals(expectedJson, response.getBody(), true);
    }

    @Test
    public void testPaginationLimit4WithOffset1OrderDESCColumnNameResponse() throws IOException, JSONException {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(GET_GAMES_URL)
                .queryParam(LIMIT_ATTRIBUTE, "4")
                .queryParam(OFFSET_ATTRIBUTE, "1")
                .queryParam(SORT_ORDER_ATTRIBUTE, "DESC")
                .queryParam(SORT_COLUMN_ATTRIBUTE, "name");

        String url = builder.toUriString();

        ResponseEntity<String> response = testRestTemplate.getForEntity(url, String.class);

        Path expectedJsonFilePath = Path.of("src/test/resources/validGetGamesResponseLimit4Offset1OrderDESCColumnNameJsonExample.json");
        String expectedJson = Files.readString(expectedJsonFilePath);

        JSONAssert.assertEquals(expectedJson, response.getBody(), true);
    }

    @Test
    public void testGetGamesSortOrderASCSortColumnPriceResponse() throws IOException, JSONException {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(GET_GAMES_URL)
                .queryParam(SORT_ORDER_ATTRIBUTE, "ASC")
                .queryParam(SORT_COLUMN_ATTRIBUTE, "price");

        String url = builder.toUriString();

        ResponseEntity<String> response = testRestTemplate.getForEntity(url, String.class);

        Path expectedJsonFilePath = Path.of("src/test/resources/validGetGamesResponseOrderASCColumnPriceJsonExample.json");
        String expectedJson = Files.readString(expectedJsonFilePath);

        JSONAssert.assertEquals(expectedJson, response.getBody(), true);
    }

    @Test
    public void testGetGamesSortOrderDESCSortColumnNameResponse() throws IOException, JSONException {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(GET_GAMES_URL)
                .queryParam(SORT_ORDER_ATTRIBUTE, "DESC")
                .queryParam(SORT_COLUMN_ATTRIBUTE, "name");

        String url = builder.toUriString();

        ResponseEntity<String> response = testRestTemplate.getForEntity(url, String.class);

        Path expectedJsonFilePath = Path.of("src/test/resources/validGetGamesResponseOrderDESCColumnNameJsonExample.json");
        String expectedJson = Files.readString(expectedJsonFilePath);

        JSONAssert.assertEquals(expectedJson, response.getBody(), true);
    }

    @Test
    public void testGetGamesSortOrderDESCSortColumnPriceResponse() throws IOException, JSONException {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(GET_GAMES_URL)
                .queryParam(SORT_ORDER_ATTRIBUTE, "DESC")
                .queryParam(SORT_COLUMN_ATTRIBUTE, "price");

        String url = builder.toUriString();

        ResponseEntity<String> response = testRestTemplate.getForEntity(url, String.class);

        Path expectedJsonFilePath = Path.of("src/test/resources/validGetGamesResponseOrderDESCColumnPriceJsonExample.json");
        String expectedJson = Files.readString(expectedJsonFilePath);

        JSONAssert.assertEquals(expectedJson, response.getBody(), true);
    }

    @Test
    public void testGetGamesSortOrderASCSortColumnNameResponse() throws IOException, JSONException {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(GET_GAMES_URL)
                .queryParam(SORT_ORDER_ATTRIBUTE, "ASC")
                .queryParam(SORT_COLUMN_ATTRIBUTE, "name");

        String url = builder.toUriString();

        ResponseEntity<String> response = testRestTemplate.getForEntity(url, String.class);

        Path expectedJsonFilePath = Path.of("src/test/resources/validGetGamesResponseOrderASCColumnNameJsonExample.json");
        String expectedJson = Files.readString(expectedJsonFilePath);

        JSONAssert.assertEquals(expectedJson, response.getBody(), true);
    }

}
