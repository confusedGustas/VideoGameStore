package com.gustas.videogamestore.dao.user;

import com.gustas.videogamestore.domain.User;
import java.util.Optional;

public interface UserDao {

    void saveUser(User user);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    User findById(Long id);
    void deleteUser(User user);
    void updateUsername(User user, String username);
    void updateEmail(User user, String email);
    void updatePassword(User user, String password);
    
}
