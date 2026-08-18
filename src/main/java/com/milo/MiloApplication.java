package com.milo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MiloApplication {
    public static void main(String[] args) {
        SpringApplication.run(MiloApplication.class, args);
        System.out.println("🚀 MILO Spring Boot Backend is running on http://localhost:8080/api");
    }
}