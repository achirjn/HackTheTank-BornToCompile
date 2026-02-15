package com.HTT.backend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.HTT.backend.entities.Company;

public interface CompanyRepo extends JpaRepository<Company, Long>{
    Optional<Company> findByEmail(String email);
    Optional<Company> findByName(String name);
    Optional<Company> findByResetPasswordToken(String token);
}
