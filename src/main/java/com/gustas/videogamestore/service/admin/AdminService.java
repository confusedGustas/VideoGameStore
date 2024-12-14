package com.gustas.videogamestore.service.admin;

import com.gustas.videogamestore.domain.UserSearchCriteria;
import com.gustas.videogamestore.dto.response.PaginatedUserListResponse;
import org.springframework.stereotype.Service;

@Service
public interface AdminService {

    void editUserState(int userId, boolean state);
    PaginatedUserListResponse getUsers(UserSearchCriteria userSearchCriteria);

}
