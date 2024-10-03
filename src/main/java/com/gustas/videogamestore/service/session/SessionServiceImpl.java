package com.gustas.videogamestore.service.session;

import com.gustas.videogamestore.dao.user.UserDao;
import com.gustas.videogamestore.domain.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SessionServiceImpl implements SessionService {

    private UserDao userDao;

    @Override
    public Long getIdFromSession() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return Long.valueOf(authentication.getName());
    }

    @Override
    public User getUserFromSessionId() {
        return userDao.findById(getIdFromSession());
    }

}
