package com.skillexchange.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import com.skillexchange.dto.JwtResponse;
import com.skillexchange.dto.LoginRequest;
import com.skillexchange.dto.RegisterRequest;
import com.skillexchange.model.User;

import com.skillexchange.repository.UserRepository;


@Service
public class AuthService {
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public AuthService(UserRepository userRepo,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       CustomUserDetailsService userDetailsService) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
authProvider.setPasswordEncoder(passwordEncoder);
this.authManager = new ProviderManager(authProvider);

    }

    public JwtResponse register(RegisterRequest req) {
        User user = User.builder()
                .username(req.getUsername())
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .role(req.getRole())
                .build();
        userRepo.save(user);
        String token = jwtService.generateToken(user.getUsername());
        return new JwtResponse(token);
    }

    public JwtResponse login(LoginRequest req) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(
                req.getUsername(), req.getPassword()));

        UserDetails userDetails = userDetailsService.loadUserByUsername(req.getUsername());
        String token = jwtService.generateToken(userDetails.getUsername());
        return new JwtResponse(token);
    }
    
}
