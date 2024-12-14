package com.gustas.videogamestore.dao.admin;

import com.gustas.videogamestore.domain.User;
import com.gustas.videogamestore.repository.AdminRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AdminDaoImpl implements AdminDao {

    private final AdminRepository adminRepository;

    @Override
    public void editUserState(int userId, boolean state) {
        adminRepository.changeUserState(userId, state);
    }

    @Override
    public Page<User> findAll(Specification<User> specification, Pageable pageable) {
        return adminRepository.findAll(specification, pageable);
    }

}
