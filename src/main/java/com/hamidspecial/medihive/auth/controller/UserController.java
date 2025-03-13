package com.hamidspecial.medihive.auth.controller;

import com.hamidspecial.medihive.auth.dto.UserDto;
import com.hamidspecial.medihive.auth.enums.Role;
import com.hamidspecial.medihive.auth.service.UserService;
import com.hamidspecial.medihive.util.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth/users")
@RequiredArgsConstructor
//@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public Result<UserDto> getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PutMapping("/{id}/role")
    public ResponseEntity<String> updateUserRole(@PathVariable Long id, @RequestParam Role newRole) {
        userService.updateUserRole(id, newRole);
        return ResponseEntity.ok("User role updated successfully");
    }

    @GetMapping("/ping")
    public Map<String, String> ping(){
        HashMap<String, String> systemCheck = new HashMap<>();
        systemCheck.put("name", "MidHive");
        systemCheck.put("timestamp", LocalDateTime.now().toString());
        systemCheck.put("version", "V1");
        return systemCheck;
    }
}

