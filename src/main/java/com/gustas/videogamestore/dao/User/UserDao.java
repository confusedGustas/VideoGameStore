package com.gustas.videogamestore.dao.User;

import com.gustas.videogamestore.domain.User;
import java.util.Optional;

public interface UserDao {

    void saveUser(User user);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    User findById(Long id);
    void deleteUser(User user);
    
}
