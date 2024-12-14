package com.gustas.videogamestore.service.admin;

import com.gustas.videogamestore.dao.admin.AdminDao;
import com.gustas.videogamestore.domain.Role;
import com.gustas.videogamestore.domain.User;
import com.gustas.videogamestore.domain.UserSearchCriteria;
import com.gustas.videogamestore.dto.response.PaginatedUserListResponse;
import com.gustas.videogamestore.dto.response.UserDetailsResponseDto;
import com.gustas.videogamestore.mapper.UserMapper;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {

    private AdminDao adminDao;
    private UserMapper userMapper;


    @Override
    public void editUserState(int userId, boolean state) {
        adminDao.editUserState(userId, state);
    }

    @Override
    public PaginatedUserListResponse getUsers(UserSearchCriteria userSearchCriteria) {
        Pageable pageable = createPageable(userSearchCriteria);
        Specification<User> specification = createUserSpecification(userSearchCriteria);

        Page<User> userPage = adminDao.findAll(specification, pageable);

        List<UserDetailsResponseDto> dtoList = userMapper.toDto(userPage.getContent());

        return new PaginatedUserListResponse(
                dtoList, userPage.getNumber(), userPage.getTotalPages(), userPage.getTotalElements());
    }

    private Specification<User> createUserSpecification(UserSearchCriteria userSearchCriteria) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            addNamePredicate(userSearchCriteria, root, criteriaBuilder, predicates);

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private void addNamePredicate(UserSearchCriteria userSearchCriteria, Root<User> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicates) {
        if (userSearchCriteria.getSearch() != null && !userSearchCriteria.getSearch().isEmpty()) {
            String[] words = userSearchCriteria.getSearch().toLowerCase().split("\\s+");
            List<Predicate> wordPredicates = new ArrayList<>();

            for (String word : words) {
                wordPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("username")), "%" + word + "%"));
            }

            predicates.add(criteriaBuilder.and(wordPredicates.toArray(new Predicate[0])));
        }
        predicates.add(criteriaBuilder.notEqual(root.get("role"), Role.ADMIN));
    }

    private Pageable createPageable(UserSearchCriteria userSearchCriteria) {
        int pageOffset = Optional.ofNullable(userSearchCriteria.getOffset()).orElse(0);
        int pageLimit = Optional.ofNullable(userSearchCriteria.getLimit()).orElse(10);

        return PageRequest.of(pageOffset, Math.min(pageLimit, 20));
    }

}
