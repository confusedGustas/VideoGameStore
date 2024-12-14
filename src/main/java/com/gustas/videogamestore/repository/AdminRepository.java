package com.gustas.videogamestore.repository;

import com.gustas.videogamestore.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface AdminRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.enabled = :state WHERE u.id = :userId")
    void changeUserState(int userId, boolean state);

}
