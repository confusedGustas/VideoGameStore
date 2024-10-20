package com.gustas.videogamestore.dao.user;

import com.gustas.videogamestore.domain.User;
import com.gustas.videogamestore.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserDaoImpl implements UserDao {

    private UserRepository userRepository;

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public User findById(Long id) {
        return userRepository.findByIdWithGames(id);
    }

    @Override
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    @Override
    public void updateUsername(User user, String username) {
        user.setUsername(username);
        userRepository.save(user);
    }

    @Override
    public void updateEmail(User user, String email) {
        user.setEmail(email);
        userRepository.save(user);
    }

    @Override
    public void updatePassword(User user, String password) {
        user.setPassword(password);
        userRepository.save(user);
    }

}
