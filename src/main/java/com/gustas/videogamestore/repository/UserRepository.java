package com.gustas.videogamestore.repository;

import com.gustas.videogamestore.domain.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Transactional
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.games WHERE u.id = :id")
    User findByIdWithGames(@Param("id") Long id);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

}
