package com.hamidspecial.medihive.auth.security;

import com.hamidspecial.medihive.auth.model.AuthUser;
import com.hamidspecial.medihive.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AuthUser> appUser = userRepository.findByUsername(username);
        return appUser.map(UserPrincipal::new).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
