package com.HTT.backend.security;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.filter.OncePerRequestFilter;

import com.HTT.backend.entities.User;
import com.HTT.backend.services.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tools.jackson.databind.ObjectMapper;

public class JwtAuthenticationFilter extends OncePerRequestFilter{

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private UserService userService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserService userService){
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
    // 1. If this is NOT the login request, pass it to the next filter and exit.
    if (!request.getServletPath().equals("/generate-token")) {
        filterChain.doFilter(request, response);
        return;
    }

    try {
        // 2. Read the email and password from the form data.
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // 3. Create an authentication token and ask the AuthenticationManager to validate it.
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password);
        Authentication authResult = authenticationManager.authenticate(authToken);

        // 4. If authentication is successful, build and send the JSON response.
        if (authResult.isAuthenticated()) {
            // Generate the JWT, including the user's roles.
            String token = jwtUtil.generateToken(authResult.getName(), authResult.getAuthorities(), 30);
            User user = (User) userService.loadUserByUsername(email);
            if(user.getAccountVerified()==0) throw new BadCredentialsException("Account not verified.");
            user.setLastActive(LocalDateTime.now());
            userService.saveUser(user);

            // Check if the user has the "ROLE_ADMIN" authority.
            boolean isAdmin = authResult.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

            // Create the response body as a Map.
            Map<String, Object> responseBody = Map.of(
                    "token", token,
                    "isAdmin", isAdmin
            );

            // Set response headers and write the JSON body.
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            new ObjectMapper().writeValue(response.getWriter(), responseBody);
        }
    } catch (AuthenticationException e) {
        // Handle failed authentication (e.g., wrong password).
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Map<String, Object> errorResponse = Map.of(
            "error", true,
            "message", "Authentication Failed: " + e.getMessage()
        );
        new ObjectMapper().writeValue(response.getWriter(), errorResponse);
    }
    }

}
