package com.gustas.videogamestore.dao.User;

import com.gustas.videogamestore.domain.User;
import com.gustas.videogamestore.repository.UserRepository;
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
    public User findById(Long id) {
        return userRepository.findByIdWithGames(id);
    }

    @Override
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

}
