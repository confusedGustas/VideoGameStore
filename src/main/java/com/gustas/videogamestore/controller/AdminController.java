package com.gustas.videogamestore.controller;

import com.gustas.videogamestore.domain.UserSearchCriteria;
import com.gustas.videogamestore.dto.response.PaginatedUserListResponse;
import com.gustas.videogamestore.service.admin.AdminService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/admin")
@AllArgsConstructor
@Tag(name = "Admin Controllers")
public class AdminController {

    private AdminService adminService;

    @PostMapping("/change-user-status")
    @ResponseStatus(HttpStatus.OK)
    public void editUserState(@RequestParam int userId, @RequestParam boolean state) {
        adminService.editUserState(userId, state);
    }

    @GetMapping("/get-users")
    @ResponseStatus(HttpStatus.OK)
    public PaginatedUserListResponse getUsers(@RequestParam(required = false) Integer limit,
                                              @RequestParam(required = false) Integer offset,
                                              @RequestParam(required = false) String search) {
        return adminService.getUsers(new UserSearchCriteria(limit, offset, search));
    }

}
