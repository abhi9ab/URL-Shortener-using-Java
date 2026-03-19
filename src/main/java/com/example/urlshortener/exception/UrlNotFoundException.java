package com.example.urlshortener.exception;

/**
 * Custom Exception: Thrown when a short code doesn't exist in the database
 * 
 * Why custom exceptions?
 * - Clearer error handling: We explicitly handle "URL not found" errors
 * - Separation of concerns: Service layer throws business logic exceptions
 * - Better error messages: Can provide context to the client
 */
public class UrlNotFoundException extends RuntimeException {

  /**
   * Constructor that takes a message
   * Example: new UrlNotFoundException("Short code 'abc123' not found")
   */
  public UrlNotFoundException(String message) {
    super(message);
  }

  /**
   * Constructor that takes message and cause
   * Allows us to preserve the original exception stack trace
   */
  public UrlNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
