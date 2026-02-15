package com.HTT.backend.services.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.HTT.backend.entities.Company;
import com.HTT.backend.repositories.CompanyRepo;
import com.HTT.backend.services.CompanyService;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyRepo companyRepository;

    @Override
    public Company loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Company> companyOptional = companyRepository.findByEmail(email);
        if (companyOptional.isEmpty()) {
            throw new UsernameNotFoundException("Company not found with email: " + email);
        }
        return companyOptional.get();
    }

    @Override
    public Company saveCompany(Company company) {
        System.out.println("About to save company to DB");
        Company saved = companyRepository.save(company);
        System.out.println("Company saved with ID: " + saved.getId());
        return saved;
    }

    @Override
    public Company findByResetPasswordToken(String token) {
        return companyRepository.findByResetPasswordToken(token).orElse(null);
    }

}
