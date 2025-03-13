package com.hamidspecial.medihive.auth.service;

import com.hamidspecial.medihive.auth.dto.UserDto;
import com.hamidspecial.medihive.auth.enums.Role;
import com.hamidspecial.medihive.auth.mapper.ValueMapper;
import com.hamidspecial.medihive.auth.model.AuthUser;
import com.hamidspecial.medihive.auth.repository.UserRepository;
import com.hamidspecial.medihive.util.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Result<UserDto> getUserById(Long id) {
        Optional<AuthUser> user = userRepository.findById(id);
        if (user.isEmpty()) throw new UsernameNotFoundException("User not found");
        UserDto userDto = ValueMapper.mapAppUserToUserDto(user.get());
        return Result.success(userDto);
    }

    public Result<UserDto> updateUserRole(Long id, Role newRole) {
        Optional<AuthUser> user = userRepository.findById(id);
        if (user.isEmpty()) throw new UsernameNotFoundException("User not found");
        AuthUser appUser = user.get();
        appUser.setRole(newRole);
        userRepository.save(appUser);
        return Result.success(appUser.getId());
    }
}
