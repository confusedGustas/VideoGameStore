package com.gustas.videogamestore.controller;

import com.gustas.videogamestore.dto.request.LoginUserRequestDto;
import com.gustas.videogamestore.dto.request.SaveUserRequestDto;
import com.gustas.videogamestore.dto.response.CheckUserResponse;
import com.gustas.videogamestore.dto.response.PaginatedResponseDto;
import com.gustas.videogamestore.dto.response.UserDetailsResponseDto;
import com.gustas.videogamestore.service.user.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/users")
@AllArgsConstructor
@Tag(name = "User Controllers")
public class UserController {

    private UserService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.OK)
    public void createUser(@Valid @RequestBody SaveUserRequestDto saveUserRequestDto) {
        userService.saveUser(saveUserRequestDto);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public void loginUser(@Valid @RequestBody LoginUserRequestDto loginUserRequestDto, HttpSession session) {
        userService.loginUser(loginUserRequestDto, session);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public void logoutUser(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        userService.logoutUser(authentication, request, response);
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(HttpServletRequest request, HttpServletResponse response) {
        userService.deleteUser(request, response);
    }

    @GetMapping("/get-details")
    @ResponseStatus(HttpStatus.OK)
    public UserDetailsResponseDto getUser() {
        return userService.getUserDetails();
    }

    @GetMapping("/get-games")
    @ResponseStatus(HttpStatus.OK)
    public PaginatedResponseDto getUserGames(@RequestParam int offset) {
        return userService.getUserGames(offset);
    }

    @GetMapping("/check")
    @ResponseStatus(HttpStatus.OK)
    public CheckUserResponse checkUser() {
        return userService.checkUser();
    }

}
