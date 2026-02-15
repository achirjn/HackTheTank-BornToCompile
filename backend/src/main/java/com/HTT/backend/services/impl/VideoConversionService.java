package com.HTT.backend.services.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Service
@Slf4j
public class VideoConversionService {

    private final RestTemplate restTemplate;
    
    @Value("${video.conversion.api.url}")
    private String conversionApiUrl;
    
    @Value("${video.conversion.api.timeout}")
    private int conversionTimeout;
    
    public VideoConversionService() {
        this.restTemplate = new RestTemplate();
    }

    /**
     * Converts a raw video file into a reel format by calling external API
     * @param videoFile The raw video file to convert
     * @return Converted reel video as byte array
     * @throws RuntimeException if conversion fails
     */
    public byte[] convertVideoToReel(MultipartFile videoFile) {
        try {
            log.info("Starting video conversion for file: {}", videoFile.getOriginalFilename());
            
            // Prepare multipart request
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            ByteArrayResource resource = new ByteArrayResource(videoFile.getBytes()) {
                @Override
                public String getFilename() {
                    return videoFile.getOriginalFilename();
                }
            };
            
            body.add("video", resource);
            body.add("format", "reel");
            body.add("quality", "high");
            
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
            
            // Call external conversion API
            log.info("Calling conversion API: {}", conversionApiUrl);
            ResponseEntity<byte[]> response = restTemplate.postForEntity(
                conversionApiUrl, 
                requestEntity, 
                byte[].class
            );
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                log.info("Video conversion successful for file: {}", videoFile.getOriginalFilename());
                return response.getBody();
            } else {
                throw new RuntimeException("Video conversion failed with status: " + response.getStatusCode());
            }
            
        } catch (IOException e) {
            log.error("Error processing video file: {}", videoFile.getOriginalFilename(), e);
            throw new RuntimeException("Failed to process video file", e);
        } catch (Exception e) {
            log.error("Video conversion failed for file: {}", videoFile.getOriginalFilename(), e);
            throw new RuntimeException("Video conversion service unavailable", e);
        }
    }
}
