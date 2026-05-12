package com.securepass.analyzer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SecurePassAnalyzerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurePassAnalyzerApplication.class, args);

        System.out.print("Successful");
    }
}
