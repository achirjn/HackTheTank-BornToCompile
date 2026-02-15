package com.HTT.backend.services;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.HTT.backend.entities.Company;

public interface CompanyService extends UserDetailsService{

    public Company saveCompany(Company company);
    public Company findByResetPasswordToken(String token);
}
