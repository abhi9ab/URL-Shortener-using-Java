package com.example.urlshortener.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Standard error response DTO
 * Provides consistent error messages to API clients
 * 
 * When an exception occurs, we return JSON like:
 * {
 * "status": 404,
 * "message": "Short code 'abc123' not found",
 * "timestamp": "2024-01-15T10:30:45"
 * }
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

  /**
   * HTTP status code (404, 400, 500, etc.)
   */
  private int status;

  /**
   * User-friendly error message
   */
  private String message;

  /**
   * When the error occurred (ISO 8601 format)
   */
  private String timestamp;
}
