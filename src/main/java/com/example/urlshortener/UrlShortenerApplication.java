package com.example.urlshortener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Application Entry Point
 * 
 * @SpringBootApplication: Meta-annotation that enables:
 *                         1. @Configuration: This class defines Spring
 *                         configuration
 *                         2. @ComponentScan: Automatically scans for Spring
 *                         components
 *                         (Controllers, Services, Repositories, etc.) in this
 *                         package and subpackages
 *                         3. @EnableAutoConfiguration: Spring Boot
 *                         automatically configures based on
 *                         classpath dependencies (e.g., H2 on classpath ->
 *                         auto-configure H2 database)
 * 
 *                         Run this main method to start the Spring Boot
 *                         application
 */
@SpringBootApplication
public class UrlShortenerApplication {

  public static void main(String[] args) {
    // SpringApplication.run() starts the embedded Tomcat server
    // and initializes the Spring application context
    SpringApplication.run(UrlShortenerApplication.class, args);
  }
}
