package com.HTT.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.HTT.backend.entities.Testimonial;

public interface TestimonialRepo extends JpaRepository<Testimonial, Long>{

}
