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

    String authHeader = request.getHeader("Authorization");

     System.out.println("📥 Request URL: " + request.getRequestURI());
    System.out.println("🔐 Authorization Header: " + authHeader);

    if (authHeader != null && authHeader.startsWith("Bearer ")) {
        String jwt = authHeader.substring(7);
        String username = jwtService.extractUsername(jwt);
          System.out.println("🧠 Extracted Username: " + username);

        // ✅ 🔽 Place debug logs here
        System.out.println("Authorization Header: " + authHeader);
        System.out.println("Extracted Username: " + username);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            var userDetails = userDetailsService.loadUserByUsername(username);

 System.out.println("✅ User from DB: " + userDetails.getUsername());
            System.out.println("✅ Token Valid? " + jwtService.isTokenValid(jwt, userDetails));

            if (jwtService.isTokenValid(jwt, userDetails)) {
                var authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                 System.out.println("✅ SecurityContext set with Authentication for " + username);
            }
        }
    }

    filterChain.doFilter(request, response);
}


}
