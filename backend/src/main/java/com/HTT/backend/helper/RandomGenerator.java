package com.HTT.backend.helper;


import java.util.Random;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RandomGenerator {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final String NUMERIC_CHARACTERS = "0123456789";

    public static String generateRandomString(int length) {
        if (length < 0) {
            throw new IllegalArgumentException("Length cannot be negative.");
        }

        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }

        return sb.toString();
    }
    
    public static String generateOTP(int length){
        if(length < 0){
            throw new IllegalArgumentException("Length cannot be negative.");
        }
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for(int i=0; i<length; i++){
            int randomIndex = random.nextInt(NUMERIC_CHARACTERS.length());
            char randomChar = NUMERIC_CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }
        return sb.toString();
    }
}

