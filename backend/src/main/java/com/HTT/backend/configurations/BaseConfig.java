package com.HTT.backend.configurations;

import javax.sql.DataSource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BaseConfig {

    @Bean
    public CommandLineRunner logDbUrl(DataSource dataSource) {
        return args -> {
            try (var conn = dataSource.getConnection()) {
                System.out.println("CONNECTED TO: " + conn.getMetaData().getURL());
            }
        };
    }
}
