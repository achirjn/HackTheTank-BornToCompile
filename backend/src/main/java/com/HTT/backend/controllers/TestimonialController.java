package com.HTT.backend.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.HTT.backend.services.impl.TestimonialReelService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/testimonials")
@RequiredArgsConstructor
public class TestimonialController {

    private final TestimonialReelService testimonialReelService;

    @PostMapping("/create")
    public ResponseEntity<?> createTestimonialAndReel(
            @RequestParam("companyName") String companyName,
            @RequestParam("file") MultipartFile file
    ) {
        try {
            var result = testimonialReelService.createTestimonialAndReel(companyName, file);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", true,
                "message", "Failed to create testimonial and reel: " + e.getMessage()
            ));
        }
    }
}

