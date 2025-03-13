package com.hamidspecial.medihive.auth.dto;

import com.hamidspecial.medihive.auth.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private long id;
    private String username;
    private String email;
    private Role role;
}
