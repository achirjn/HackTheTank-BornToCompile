package com.HTT.backend.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "companies")
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class Company implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    @Column(nullable = true)
    private String verificationToken;

    @Column(nullable = true)
    private String resetPasswordToken;

    @Column(nullable = true)
    private LocalDateTime tokenExpirationTime;

    private int accountVerified;

    @Column(nullable = false)
    private Boolean adminPermit = false;

    private LocalDateTime lastActive;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<Testimonial> testimonials = new ArrayList<>();

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<Reel> reels = new ArrayList<>();

    // Constructors
    public Company(String name, String email, String password, int accountVerified) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.adminPermit = false;
        this.accountVerified = accountVerified;
    }

    public Company(String name, String email, String password, String verificationToken, int accountVerified) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.verificationToken = verificationToken;
        this.adminPermit = false;
        this.accountVerified = accountVerified;
    }

    public Company(Long id, String name, String email, String password, int accountVerified) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.accountVerified = accountVerified;
        this.adminPermit = false;
    }

    public Company(Long id, String name, String email, String password, String verificationToken, int accountVerified) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.verificationToken = verificationToken;
        this.accountVerified = accountVerified;
        this.adminPermit = false;
    }

    public Company(String name, String email, String password, boolean adminPermit, int accountVerified) {
        this.adminPermit = adminPermit;
        this.email = email;
        this.name = name;
        this.password = password;
        this.accountVerified = accountVerified;
    }

    // UserDetails implementation
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.adminPermit) {
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else {
            return List.of(new SimpleGrantedAuthority("ROLE_COMPANY"));
        }
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.accountVerified == 1;
    }
}
