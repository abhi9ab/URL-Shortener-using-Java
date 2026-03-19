package com.example.urlshortener.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) for URL Shortening Request
 * 
 * WHY DTOs?
 * - Don't expose database entity directly to API clients
 * - Clients only see fields we want to expose
 * - Decouples API contract from database structure
 * - If we change database schema, API doesn't break
 * - Security: Prevents accidental exposure of internal fields
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UrlShortenRequest {

  /**
   * url: The full URL that user wants to shorten
   * Example: "https://stackoverflow.com/questions/12345/how-to-shorten-urls"
   */
  private String url;
}
