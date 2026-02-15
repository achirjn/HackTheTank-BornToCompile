package com.HTT.backend.services.impl;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.HTT.backend.entities.Company;
import com.HTT.backend.entities.Testimonial;
import com.HTT.backend.entities.Reel;
import com.HTT.backend.repositories.CompanyRepo;
import com.HTT.backend.repositories.TestimonialRepo;
import com.HTT.backend.repositories.ReelRepo;
import com.cloudinary.Cloudinary;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TestimonialReelService {

    private final CompanyRepo companyRepository;
    private final TestimonialRepo testimonialRepository;
    private final ReelRepo reelRepository;
    private final Cloudinary cloudinary;
    private final VideoConversionService videoConversionService;

    /**
     * Creates both testimonial and reel from a single video upload
     * @param companyName The name of the company
     * @param videoFile The video file to process
     * @return Map containing both testimonial and reel information
     */
    @Transactional
    public Map<String, Object> createTestimonialAndReel(String companyName, MultipartFile videoFile) {
        try {
            log.info("Starting testimonial and reel creation for company: {}", companyName);
            
            // Find company by name
            Company company = companyRepository.findByName(companyName)
                .orElseThrow(() -> new RuntimeException("Company not found with name: " + companyName));
            
            // Step 1: Upload original video to Cloudinary for testimonial
            String testimonialUrl = uploadVideoToCloudinary(videoFile);
            log.info("Testimonial video uploaded to Cloudinary: {}", testimonialUrl);
            
            // Step 2: Create testimonial entity
            Testimonial testimonial = new Testimonial();
            testimonial.setUrl(testimonialUrl);
            testimonial.setCompany(company);
            Testimonial savedTestimonial = testimonialRepository.save(testimonial);
            log.info("Testimonial created with ID: {}", savedTestimonial.getId());
            
            // Step 3: Convert video to reel using external service
            byte[] convertedReelBytes = videoConversionService.convertVideoToReel(videoFile);
            log.info("Video converted to reel format");
            
            // Step 4: Upload converted reel to Cloudinary
            String reelUrl = uploadConvertedReelToCloudinary(convertedReelBytes, videoFile.getOriginalFilename());
            log.info("Reel uploaded to Cloudinary: {}", reelUrl);
            
            // Step 5: Create reel entity with same company
            Reel reel = new Reel();
            reel.setUrl(reelUrl);
            reel.setCompany(company);
            Reel savedReel = reelRepository.save(reel);
            log.info("Reel created with ID: {}", savedReel.getId());
            
            return Map.of(
                "testimonial", Map.of(
                    "id", savedTestimonial.getId(),
                    "url", savedTestimonial.getUrl(),
                    "companyId", savedTestimonial.getCompany().getId()
                ),
                "reel", Map.of(
                    "id", savedReel.getId(),
                    "url", savedReel.getUrl(),
                    "companyId", savedReel.getCompany().getId(),
                    "createdAt", savedReel.getCreatedAt()
                ),
                "message", "Testimonial and reel created successfully"
            );
            
        } catch (Exception e) {
            log.error("Failed to create testimonial and reel for company: {}", companyName, e);
            throw new RuntimeException("Failed to process video and create testimonial/reel", e);
        }
    }

    /**
     * Uploads original video file to Cloudinary
     */
    private String uploadVideoToCloudinary(MultipartFile file) throws IOException {
        Map<?, ?> uploadResult = cloudinary.uploader().upload(
            file.getBytes(),
            Map.of(
                "resource_type", "video",
                "folder", "testimonials"
            )
        );
        return uploadResult.get("secure_url").toString();
    }

    /**
     * Uploads converted reel bytes to Cloudinary
     */
    private String uploadConvertedReelToCloudinary(byte[] reelBytes, String originalFilename) throws IOException {
        Map<?, ?> uploadResult = cloudinary.uploader().upload(
            reelBytes,
            Map.of(
                "resource_type", "video",
                "folder", "reels",
                "public_id", "reel_" + System.currentTimeMillis(),
                "format", "mp4"
            )
        );
        return uploadResult.get("secure_url").toString();
    }
}
