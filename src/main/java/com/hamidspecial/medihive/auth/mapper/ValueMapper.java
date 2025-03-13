package com.hamidspecial.medihive.auth.mapper;

import com.hamidspecial.medihive.auth.dto.UserDto;
import com.hamidspecial.medihive.auth.model.AuthUser;

public class ValueMapper {

    private ValueMapper(){}

    public static UserDto mapAppUserToUserDto(AuthUser appUser){
        UserDto userDto = new UserDto();
        userDto.setId(appUser.getId());
        userDto.setEmail(appUser.getEmail());
        userDto.setRole(appUser.getRole());
        userDto.setUsername(appUser.getUsername());
        return userDto;
    }
}
