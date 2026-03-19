package com.example.urlshortener.service;

import com.example.urlshortener.model.Url;
import com.example.urlshortener.repository.UrlRepository;
import com.example.urlshortener.exception.UrlNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Random;

/**
 * Service Layer: Business Logic
 * 
 * Responsibilities:
 * 1. Validate input (URL format)
 * 2. Generate short codes
 * 3. Save to database via repository
 * 4. Fetch URLs and update click counts
 * 
 * @Service: Spring annotation marking this as a service component
 *           Tells Spring to manage this class and inject dependencies
 */
@Service
public class UrlService {

  /**
   * UrlRepository: Database access layer
   * 
   * @Autowired: Spring automatically injects this dependency
   *             Dependency Injection: Instead of creating new UrlRepository(),
   *             Spring provides it (loose coupling, easier testing)
   */
  @Autowired
  private UrlRepository urlRepository;

  // Character set for generating short codes
  // Base62: Numbers (0-9) + Lowercase (a-z) + Uppercase (A-Z) = 62 characters
  // More compact than Base10 (shorter codes for same range)
  private static final String CHARACTERS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
  private static final int SHORT_CODE_LENGTH = 6;
  private static final Random RANDOM = new Random();

  /**
   * Shorten a URL: Main business logic
   * 
   * Process:
   * 1. Validate the URL
   * 2. Generate unique short code
   * 3. Create Url entity
   * 4. Save to database
   * 5. Return saved entity
   */
  public Url shortenUrl(String originalUrl) {
    // Step 1: Validate URL format
    validateUrl(originalUrl);

    // Step 2: Generate unique short code
    String shortCode = generateUniqueShortCode();

    // Step 3: Create Url entity
    Url url = new Url();
    url.setOriginalUrl(originalUrl);
    url.setShortCode(shortCode);
    // createdAt is set automatically by @PrePersist in Url entity
    // clickCount is initialized to 0 by @PrePersist

    // Step 4: Save to database
    Url savedUrl = urlRepository.save(url);

    // Step 5: Return the saved entity (now has an ID)
    return savedUrl;
  }

  /**
   * Get original URL by short code
   * 
   * Process:
   * 1. Query database for the short code
   * 2. If found, increment click count and update lastAccessedAt
   * 3. Return the URL
   * 4. If not found, throw exception
   */
  public Url getUrlByShortCode(String shortCode) {
    // Query database
    Url url = urlRepository.findByShortCode(shortCode)
        .orElseThrow(() -> new UrlNotFoundException(
            "Short code '" + shortCode + "' not found"));

    // BONUS: Update click analytics
    // Increment click count
    url.setClickCount(url.getClickCount() + 1);
    // Update last access time
    url.setLastAccessedAt(LocalDateTime.now());
    // Save changes to database
    urlRepository.save(url);

    return url;
  }

  /**
   * Get click statistics for a URL
   */
  public Url getUrlStats(String shortCode) {
    return urlRepository.findByShortCode(shortCode)
        .orElseThrow(() -> new UrlNotFoundException(
            "Short code '" + shortCode + "' not found"));
  }

  /**
   * Validate URL format
   * 
   * Basic validation: Check if URL starts with http:// or https://
   * In production, you'd use regex or Apache Commons Validator
   */
  private void validateUrl(String url) {
    if (url == null || url.trim().isEmpty()) {
      throw new IllegalArgumentException("URL cannot be empty");
    }

    if (!url.startsWith("http://") && !url.startsWith("https://")) {
      throw new IllegalArgumentException(
          "URL must start with http:// or https://");
    }

    if (url.length() > 2048) {
      throw new IllegalArgumentException(
          "URL is too long (max 2048 characters)");
    }
  }

  /**
   * Generate a unique short code
   * 
   * Algorithm:
   * 1. Generate random 6-character code from Base62 charset
   * 2. Check if it already exists in database
   * 3. If exists, retry (generate new code)
   * 4. If doesn't exist, return it (guaranteed unique)
   * 
   * Probability of collision is very low with Base62 + 6 chars
   * Chance of collision: ~1 in 56 billion (theoretically safe)
   */
  private String generateUniqueShortCode() {
    while (true) {
      String shortCode = generateRandomCode();
      // Check if this short code already exists
      if (urlRepository.findByShortCode(shortCode).isEmpty()) {
        return shortCode; // Found a unique one
      }
      // If exists, loop will generate a new one
    }
  }

  /**
   * Generate a random code using Base62 characters
   * 
   * Example output: "a1B2c3"
   */
  private String generateRandomCode() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < SHORT_CODE_LENGTH; i++) {
      int randomIndex = RANDOM.nextInt(CHARACTERS.length());
      sb.append(CHARACTERS.charAt(randomIndex));
    }
    return sb.toString();
  }
}
