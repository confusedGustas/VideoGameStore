package com.gustas.videogamestore.service.Session;

import com.gustas.videogamestore.domain.User;
import org.springframework.stereotype.Service;

@Service
public interface SessionService {

    Long getIdFromSession();
    User getUserFromSessionId();

}
