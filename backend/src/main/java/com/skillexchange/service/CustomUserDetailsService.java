package com.skillexchange.service;

import com.skillexchange.model.User;
import com.skillexchange.repository.UserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository repo) {
        this.userRepository = repo;
    }

   @Override
public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

    if (!user.isActive()) {
        throw new DisabledException("User is blocked");
    }

    // ✅ Debug logs
    System.out.println("👤 Found User: " + user.getUsername());
    System.out.println("🔐 Role: " + user.getRole());
    System.out.println("✅ Final Authority: ROLE_" + user.getRole().name());

   return new CustomUserDetails(user);

}

}
