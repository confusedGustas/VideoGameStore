package com.gustas.videogamestore.controller;

import com.gustas.videogamestore.AbstractIntegrationTest;
import com.gustas.videogamestore.TestConfiguration;
import com.gustas.videogamestore.dto.request.LoginUserRequestDto;
import com.gustas.videogamestore.dto.request.SaveUserRequestDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

import static com.gustas.videogamestore.constants.TestConstants.LOGIN_USER_URL;
import static com.gustas.videogamestore.constants.TestConstants.REGISTER_USER_URL;

@ContextConfiguration(classes = {TestConfiguration.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest extends AbstractIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void testValidRegisterUserResponse() {
        SaveUserRequestDto saveUserRequestDto = new SaveUserRequestDto(
                "testUser",
                "testPassword123!",
                "testEmail@gmail.com"
        );

        ResponseEntity<String> postResponse = testRestTemplate.postForEntity(
                REGISTER_USER_URL,
                saveUserRequestDto,
                String.class
        );

        Assertions.assertEquals(HttpStatus.OK, postResponse.getStatusCode());
    }

    @Test
    void testInvalidRegisterUserResponse() {
        SaveUserRequestDto saveUserRequestDto = new SaveUserRequestDto(
                "testUser",
                "testPassword",
                "testEmail.com"
        );

        ResponseEntity<String> postResponse = testRestTemplate.postForEntity(
                REGISTER_USER_URL,
                saveUserRequestDto,
                String.class
        );

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, postResponse.getStatusCode());
    }

    @Test
    void testLoginUserResponse() {
        LoginUserRequestDto loginUserRequestDto = new LoginUserRequestDto(
                "Test",
                "Test123!"
        );

        ResponseEntity<String> postResponse = testRestTemplate.postForEntity(
                LOGIN_USER_URL,
                loginUserRequestDto,
                String.class
        );

        Assertions.assertEquals(HttpStatus.OK, postResponse.getStatusCode());
    }

}
