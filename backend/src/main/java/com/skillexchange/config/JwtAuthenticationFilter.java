package com.skillexchange.config;

import com.skillexchange.service.CustomUserDetailsService;
import com.skillexchange.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, CustomUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        System.out.println("🔎 JwtAuthenticationFilter triggered");

       String authHeader = request.getHeader("Authorization");
if (authHeader == null || !authHeader.startsWith("Bearer ")) {
    log.warn("❌ Missing or invalid Authorization header");
    filterChain.doFilter(request, response);
    return;
}

String jwt = authHeader.substring(7);
String username = jwtService.extractUsername(jwt); // Extract from token

log.info("🔐 Extracted username from token: {}", username);

// Check if already authenticated
if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
    if (jwtService.isTokenValid(jwt, userDetails)) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);
        log.info("✅ Authenticated user: {}", username);
    } else {
        log.warn("❌ Invalid token for user: {}", username);
    }
}


        try {
    username = jwtService.extractUsername(jwt);
} catch (Exception e) {
    System.out.println("❌ Failed to extract username from JWT: " + e.getMessage());
    // ❗ Let the request continue without setting SecurityContext
    filterChain.doFilter(request, response);
    return;
}


        System.out.println("🧠 Extracted Username: " + username);

        if (username == null) {
            System.out.println("❌ Username is null after extraction.");
            filterChain.doFilter(request, response);
            return;
        }

        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            System.out.println("⚠️ Authentication already present in SecurityContext.");
            filterChain.doFilter(request, response);
            return;
        }

        var userDetails = userDetailsService.loadUserByUsername(username);
        System.out.println("✅ Loaded User from DB: " + userDetails.getUsername());

        boolean isValid = jwtService.isTokenValid(jwt, userDetails);
        System.out.println("🔍 JWT Token Valid? " + isValid);

      if (!isValid) {
    System.out.println("❌ JWT is invalid for user: " + username);
    filterChain.doFilter(request, response); // ✅ let it go through
    return;
}


        var authToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);

        System.out.println("✅ SecurityContext updated for user: " + username);

        filterChain.doFilter(request, response);
    }
}
