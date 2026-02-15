package com.HTT.backend.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.HTT.backend.entities.Company;
import com.HTT.backend.services.CompanyService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final CompanyService companyService;
    private JwtUtil jwtUtil;
    private PasswordEncoder passwordEncoder;
    
    @Value("${frontend.base.url}")
    private String frontendUrl;

    public OAuthAuthenticationSuccessHandler(JwtUtil jwtUtil,@Lazy PasswordEncoder passwwEncoder, CompanyService companyService) {
        this.jwtUtil = jwtUtil;
        this.companyService = companyService;
        this.passwordEncoder = passwwEncoder;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        try {
            DefaultOAuth2User oAuthUser = (DefaultOAuth2User) authentication.getPrincipal();
            String name = oAuthUser.getAttribute("name").toString();
            String email = oAuthUser.getAttribute("email").toString();
            System.out.println(name + "  " + email);

            Company company;
            try {
                company = (Company) companyService.loadUserByUsername(email);
            } catch (UsernameNotFoundException e) {
                company = null;
            }
            if (company == null) {
                System.out.println("new company");
                String encodedPassword = passwordEncoder.encode("gw(8ehnbeiub*(*7766hspoiaw)(^6sa5&s*%78iofgwskl23gs");
                company = new Company(name, email, encodedPassword, email.equals("achirjain11@gmail.com"), 1);
                company = companyService.saveCompany(company);
                System.out.println("saved company: " + company);
            }
            String jwtToken = jwtUtil.generateToken(company.getEmail(), company.getAuthorities(), 15L);
            System.out.println("token: " + jwtToken);
            boolean isAdmin = company.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

            String redirectUrl = UriComponentsBuilder.fromUriString(frontendUrl)
                    .queryParam("token", jwtToken)
                    .queryParam("isAdmin", isAdmin)
                    .build().toUriString();
            System.out.println("redirect to " + redirectUrl);

            new DefaultRedirectStrategy().sendRedirect(request, response, redirectUrl);
            // --- END OF EXISTING LOGIC ---

        } catch (Exception e) {
            // THIS WILL CATCH ANY UNEXPECTED ERROR
            System.err.println("!!!!!!!!!! CRITICAL OAUTH FAILURE !!!!!!!!!!");
            e.printStackTrace(); // This prints the full error stack trace to the log

            // Redirect to a generic error page so the user isn't stuck
            String errorUrl = UriComponentsBuilder.fromUriString(frontendUrl+"/login")
                    .queryParam("error", "LoginProcessingFailed")
                    .build().toUriString();
            new DefaultRedirectStrategy().sendRedirect(request, response, errorUrl);
        }
    }

}
