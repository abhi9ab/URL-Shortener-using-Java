package com.example.urlshortener.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for URL click analytics response
 * Provides information about how many times a URL was accessed
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UrlClickResponse {

  /**
   * shortCode: The short code that was clicked
   */
  private String shortCode;

  /**
   * originalUrl: The URL being tracked
   */
  private String originalUrl;

  /**
   * clickCount: Number of times this URL was accessed
   */
  private Long clickCount;

  /**
   * message: Informational message
   */
  private String message;
}
