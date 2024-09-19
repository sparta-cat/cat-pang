package com.catpang.user.presentation.internal;

import com.catpang.core.application.dto.UserDto;
import com.catpang.user.application.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/internal/users")
public class UserInternalControllerImpl {

    private final UserService userService;

    @GetMapping("/{id}")
    public UserDto.Result getUser(@PathVariable String id) {
        return userService.getUser(id);
    }
}