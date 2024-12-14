package com.gustas.videogamestore.service.user;

import com.gustas.videogamestore.dao.game.GameDao;
import com.gustas.videogamestore.dao.user.UserDao;
import com.gustas.videogamestore.domain.Game;
import com.gustas.videogamestore.domain.Role;
import com.gustas.videogamestore.domain.User;
import com.gustas.videogamestore.dto.request.ChangeUserEmailDto;
import com.gustas.videogamestore.dto.request.ChangeUserPasswordDto;
import com.gustas.videogamestore.dto.request.ChangeUserUsernameDto;
import com.gustas.videogamestore.dto.request.LoginUserRequestDto;
import com.gustas.videogamestore.dto.request.SaveUserRequestDto;
import com.gustas.videogamestore.dto.response.CheckUserResponseDto;
import com.gustas.videogamestore.dto.response.GameResponseDto;
import com.gustas.videogamestore.dto.response.PaginatedResponseDto;
import com.gustas.videogamestore.dto.response.UserDetailsResponseDto;
import com.gustas.videogamestore.mapper.GameMapper;
import com.gustas.videogamestore.mapper.UserMapper;
import com.gustas.videogamestore.service.session.SessionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
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
import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserDao userDao;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private SessionService sessionService;
    private GameDao gameDao;
    private UserMapper userMapper;
    private GameMapper gameMapper;

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
    public UserDetailsResponseDto getUserDetails() {
        return userMapper.toDto(sessionService.getUserFromSessionId());
    }

    @Override
    public PaginatedResponseDto getUserGames(int pageOffset) {
        Specification<Game> userSpec = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("user").get("id"), sessionService.getUserFromSessionId().getId());

        Page<Game> gamePage = gameDao.findAll(userSpec, PageRequest.of(pageOffset, 10));

        List<GameResponseDto> dtoList = gameMapper.toDto(gamePage.getContent());

        return new PaginatedResponseDto(
                dtoList, gamePage.getNumber(), gamePage.getTotalPages(), gamePage.getTotalElements());
    }

    @Override
    public CheckUserResponseDto checkUser() {
        try {
            User user = sessionService.getUserFromSessionId();
            boolean isAdmin = user.getRole() == Role.ADMIN;

            return buildCheckUserResponse(true, isAdmin);
        } catch (Exception e) {
            return buildCheckUserResponse(false, false);
        }
    }

    @Override
    public void changeUsername(Authentication authentication, HttpServletRequest request, HttpServletResponse response,
                               ChangeUserUsernameDto changeUserUsernameDto) {
        userDao.updateUsername(sessionService.getUserFromSessionId(), changeUserUsernameDto.getUsername());
        logoutUser(authentication, request, response);
    }

    @Override
    public void changePassword(Authentication authentication, HttpServletRequest request, HttpServletResponse response,
                               ChangeUserPasswordDto changeUserPasswordDto) {
        userDao.updatePassword(sessionService.getUserFromSessionId(), encodeUserPassword(changeUserPasswordDto.getPassword()));
        logoutUser(authentication, request, response);
    }

    @Override
    public void changeEmail(Authentication authentication, HttpServletRequest request, HttpServletResponse response,
                            ChangeUserEmailDto changeUserEmailDto) {
        userDao.updateEmail(sessionService.getUserFromSessionId(), changeUserEmailDto.getEmail());
        logoutUser(authentication, request, response);
    }

    @Override
    public void saveUser(SaveUserRequestDto saveUserRequestDto) {
        validateRegistrationCredentials(saveUserRequestDto);

        User user = createUserEntity(saveUserRequestDto);

        userDao.saveUser(user);
    }

    private CheckUserResponseDto buildCheckUserResponse(boolean isUserLoggedIn, boolean isAdmin) {
        return new CheckUserResponseDto(isUserLoggedIn, isAdmin);
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
            throw new IllegalArgumentException("User is not enabled");
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
        User user = userMapper.toEntity(saveUserRequestDto);

        user.setPassword(encodeUserPassword(user.getPassword()));
        user.setRole(Role.USER);
        user.setEnabled(true);

        return user;
    }

    private String encodeUserPassword(String password) {
        return bCryptPasswordEncoder.encode(password);
    }

}
