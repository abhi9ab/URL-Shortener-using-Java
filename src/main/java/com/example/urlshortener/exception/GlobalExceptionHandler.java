package com.example.urlshortener.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Global Exception Handler
 * 
 * @ControllerAdvice: Spring annotation that handles exceptions globally
 *                    - Instead of try-catch in every controller method
 *                    - All exceptions of certain types are caught here
 *                    - Provides consistent error responses across entire API
 * 
 *                    WHY centralized exception handling?
 *                    - DRY (Don't Repeat Yourself): One place to handle errors
 *                    - Consistency: All errors return same JSON format
 *                    - Cleaner code: No try-catch blocks in controllers
 *                    - Easier maintenance: Change error format in one place
 */
@ControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Handle UrlNotFoundException
   * 
   * @ExceptionHandler: This method handles UrlNotFoundException
   *                    When UrlNotFoundException is thrown anywhere, Spring calls
   *                    this method
   */
  @ExceptionHandler(UrlNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleUrlNotFoundException(UrlNotFoundException ex) {
    ErrorResponse errorResponse = new ErrorResponse(
        404,
        ex.getMessage(),
        LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
  }

  /**
   * Handle illegal arguments (e.g., invalid URL format)
   */
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
    ErrorResponse errorResponse = new ErrorResponse(
        400,
        ex.getMessage(),
        LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  /**
   * Generic exception handler for unexpected errors
   * This is the fallback for any exception not handled above
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
    ErrorResponse errorResponse = new ErrorResponse(
        500,
        "An unexpected error occurred: " + ex.getMessage(),
        LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
  }
}
