package com.skillexchange.service;
import org.springframework.security.core.Authentication;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.context.SecurityContextHolder;
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
    // Check if a user already exists with the given email
    if (userRepo.findByEmail(req.getEmail()).isPresent()) {
        throw new IllegalArgumentException("Email already registered.");
    }

    // Optional: also check for duplicate username
    if (userRepo.findByUsername(req.getUsername()).isPresent()) {
        throw new IllegalArgumentException("Username already taken.");
    }

    // Create and save the new user
    User user = User.builder()
            .username(req.getUsername())
            .email(req.getEmail())
            .password(passwordEncoder.encode(req.getPassword()))
            .role(req.getRole())
            .build();

    userRepo.save(user);

    // Generate JWT token
   String token = jwtService.generateToken(user.getUsername(), user.getId(), user.getRole().name());


    return new JwtResponse(token, user.getRole().name());

}


  public JwtResponse login(LoginRequest req) {
    // 1. Authenticate the user
    UsernamePasswordAuthenticationToken authToken =
        new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword());

    Authentication authentication = authManager.authenticate(authToken);

    // ✅ 2. Set the authenticated user in the SecurityContext
    SecurityContextHolder.getContext().setAuthentication(authentication);

    // 3. Generate JWT from authenticated user
   CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
String token = jwtService.generateToken(userDetails.getUsername(), userDetails.getId(), userDetails.getRole());



   return new JwtResponse(token,userDetails.getRole());

}



    
}
