package com.catpang.user.application.service;

import com.catpang.core.application.dto.UserDto;

public interface UserService {

    UserDto.Result join(UserDto.Create userCreate);

    UserDto.Result getUser(String userId);

    UserDto.Result updateUser(String userId, UserDto.Update update);

    UserDto.Delete.Result deleteUser(String userId);
}
