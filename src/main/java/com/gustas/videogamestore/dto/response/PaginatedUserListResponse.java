package com.gustas.videogamestore.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaginatedUserListResponse {

    private List<UserDetailsResponseDto> users;
    private Integer totalPages;
    private Integer currentPage;
    private Long totalUsers;

}
