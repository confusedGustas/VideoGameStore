package com.gustas.videogamestore.dao.admin;

import com.gustas.videogamestore.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface AdminDao {

    void editUserState(int userId, boolean state);
    Page<User> findAll(Specification<User> specification, Pageable pageable);

}
