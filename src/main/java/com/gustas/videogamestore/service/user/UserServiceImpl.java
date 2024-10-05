package com.gustas.videogamestore.service.user;

import com.gustas.videogamestore.dao.user.UserDao;
import com.gustas.videogamestore.domain.Role;
import com.gustas.videogamestore.domain.User;
import com.gustas.videogamestore.dto.request.LoginUserRequestDto;
import com.gustas.videogamestore.dto.request.SaveUserRequestDto;
import com.gustas.videogamestore.dto.response.CheckUserResponse;
import com.gustas.videogamestore.dto.response.UserResponseDto;
import com.gustas.videogamestore.mapper.UserMapper;
import com.gustas.videogamestore.service.session.SessionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserDao userDao;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private SessionService sessionService;

    @Override
    public void loginUser(LoginUserRequestDto loginUserRequestDto, HttpSession session) {
        User user = validateLoginCredentials(loginUserRequestDto);
        authenticateUser(user, session);
    }

    @Override
    public void logoutUser(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(request, response, authentication);
    }

    @Override
    public UserResponseDto getUserDetails() {
        return UserMapper.toDto(sessionService.getUserFromSessionId());
    }

    @Override
    public void deleteUser(HttpServletRequest request, HttpServletResponse response) {
        User user = sessionService.getUserFromSessionId();
        userDao.deleteUser(user);

        logoutUser(null, request, response);
    }

    @Override
    public CheckUserResponse checkUser() {
        try {
            sessionService.getUserFromSessionId();
            return buildCheckUserResponse(true);
        } catch (Exception e) {
            return buildCheckUserResponse(false);
        }
    }

    @Override
    public void saveUser(SaveUserRequestDto saveUserRequestDto) {
        validateRegistrationCredentials(saveUserRequestDto);

        User user = createUserEntity(saveUserRequestDto);

        userDao.saveUser(user);
    }

    private CheckUserResponse buildCheckUserResponse(boolean isUserLoggedIn) {
        return new CheckUserResponse(isUserLoggedIn);
    }

    public void authenticateUser(User user, HttpSession session) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = getAuthentication(user, securityContext.getAuthentication());

        SecurityContextHolder.getContext().setAuthentication(authentication);
        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
    }

    private static Authentication getAuthentication(User user, Authentication existingAuth) {
        if (existingAuth != null && existingAuth.isAuthenticated() && !(existingAuth instanceof AnonymousAuthenticationToken)) {
            throw new IllegalArgumentException("User is already authenticated");
        }

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(user.getRole().name());

        return new UsernamePasswordAuthenticationToken(
                user.getId(),
                user.getPassword(),
                Collections.singletonList(simpleGrantedAuthority));
    }

    private User validateLoginCredentials(LoginUserRequestDto loginUserRequestDto) {
        User user = userDao.findByUsername(loginUserRequestDto.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Username"));

        if (!user.isEnabled()) {
            throw new IllegalArgumentException("User is not enabled, please confirm your email");
        }

        if (!bCryptPasswordEncoder.matches(loginUserRequestDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid Password");
        }

        return user;
    }

    private void validateRegistrationCredentials(SaveUserRequestDto saveUserRequestDto) {
        if (userDao.findByUsername(saveUserRequestDto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        if (userDao.findByEmail(saveUserRequestDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }
    }

    private User createUserEntity(SaveUserRequestDto saveUserRequestDto) {
        User user = UserMapper.toEntity(saveUserRequestDto);

        user.setPassword(encodeUserPassword(user.getPassword()));
        user.setRole(Role.USER);
        user.setEnabled(true);

        return user;
    }

    private String encodeUserPassword(String password) {
        return bCryptPasswordEncoder.encode(password);
    }

}
