package com.example.urlshortener.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) for URL Shortening Response
 * 
 * This is what the API returns to the client after shortening a URL
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UrlShortenResponse {

  /**
   * shortUrl: The complete short URL the user can share
   * Example: "http://localhost:8080/abc123"
   */
  private String shortUrl;

  /**
   * shortCode: Just the short code part (useful for copying)
   * Example: "abc123"
   */
  private String shortCode;

  /**
   * originalUrl: Echo back the original URL (for confirmation)
   */
  private String originalUrl;
}
