package com.skillexchange.config;

import com.skillexchange.service.CustomUserDetailsService;
import com.skillexchange.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

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
        String requestURI = request.getRequestURI();

        System.out.println("\n📥 Incoming Request URL: " + requestURI);
        System.out.println("🔐 Authorization Header: " + authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("❌ No Bearer token found or invalid Authorization header.");
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(7);
        String username = null;

        try {
            username = jwtService.extractUsername(jwt);
        } catch (Exception e) {
            System.out.println("❌ Failed to extract username from JWT: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
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
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
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
