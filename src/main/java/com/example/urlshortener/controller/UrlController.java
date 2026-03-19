package com.example.urlshortener.controller;

import com.example.urlshortener.model.Url;
import com.example.urlshortener.service.UrlService;
import com.example.urlshortener.dto.UrlShortenRequest;
import com.example.urlshortener.dto.UrlShortenResponse;
import com.example.urlshortener.dto.UrlClickResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * REST API Controller
 * 
 * Handles HTTP requests and returns JSON responses
 * 
 * @RestController: Spring annotation that marks this as a REST controller
 *                  - Combines @Controller + @ResponseBody
 *                  - All methods automatically return JSON
 * 
 *                  @RequestMapping("/api/v1"): Base path for all endpoints
 *                  - All paths start with /api/v1
 *                  - Version the API for future compatibility
 */
@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*") // Allow requests from any origin (for frontend)
public class UrlController {

  /**
   * UrlService: Business logic layer
   * Dependency Injection: Spring provides this automatically
   */
  @Autowired
  private UrlService urlService;

  /**
   * Endpoint 1: Shorten a URL
   * 
   * HTTP Method: POST (creating a new resource)
   * Path: POST /api/v1/shorten
   * Request Body: { "url": "https://example.com/very-long-url" }
   * Response: { "shortUrl": "http://localhost:8080/abc123", "shortCode":
   * "abc123", ... }
   * Status: 201 Created (new resource created)
   * 
   * @PostMapping: Maps to HTTP POST request
   * @RequestBody: Automatically deserialize JSON to UrlShortenRequest object
   *               Returns: ResponseEntity with HTTP status and response body
   */
  @PostMapping("/shorten")
  public ResponseEntity<UrlShortenResponse> shortenUrl(
      @RequestBody UrlShortenRequest request) {

    // Step 1: Validate input
    if (request.getUrl() == null || request.getUrl().isEmpty()) {
      return ResponseEntity.badRequest().build();
    }

    // Step 2: Call service to shorten URL
    Url savedUrl = urlService.shortenUrl(request.getUrl());

    // Step 3: Build response DTO
    UrlShortenResponse response = new UrlShortenResponse();
    // Build full short URL
    response.setShortUrl("http://localhost:8080/" + savedUrl.getShortCode());
    response.setShortCode(savedUrl.getShortCode());
    response.setOriginalUrl(savedUrl.getOriginalUrl());

    // Step 4: Return response with 201 Created status
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  /**
   * Endpoint 2: Redirect to original URL
   * 
   * HTTP Method: GET (retrieve a resource)
   * Path: GET /{shortCode}
   * Example: GET /abc123
   * Action: Redirect to original URL
   * Status: 302 Found (redirect)
   * 
   * @PathVariable: Extract shortCode from URL path
   *                Example: /api/v1/redirect/abc123 -> shortCode = "abc123"
   * 
   *                HttpServletResponse: Low-level HTTP response object
   *                Used for sending redirect headers
   */
  @GetMapping("/redirect/{shortCode}")
  public void redirectToOriginalUrl(
      @PathVariable String shortCode,
      HttpServletResponse response) throws IOException {

    // Step 1: Fetch original URL from database
    // This also increments the click count (see UrlService.getUrlByShortCode)
    Url url = urlService.getUrlByShortCode(shortCode);

    // Step 2: Send HTTP 302 redirect to original URL
    response.sendRedirect(url.getOriginalUrl());
  }

  /**
   * Alternative redirect endpoint (shorter path)
   * Path: GET /{shortCode}
   * This allows users to just visit http://localhost:8080/abc123
   * without needing /api/v1/redirect prefix
   */
  @GetMapping("/{shortCode}")
  public void directRedirect(
      @PathVariable String shortCode,
      HttpServletResponse response) throws IOException {

    Url url = urlService.getUrlByShortCode(shortCode);
    response.sendRedirect(url.getOriginalUrl());
  }

  /**
   * Endpoint 3: Get click statistics
   * 
   * Path: GET /api/v1/stats/{shortCode}
   * Response: Shows how many times URL was accessed
   * 
   * BONUS Feature: Analytics
   * Track engagement of shortened URLs
   */
  @GetMapping("/stats/{shortCode}")
  public ResponseEntity<UrlClickResponse> getUrlStats(
      @PathVariable String shortCode) {

    // Fetch URL stats
    Url url = urlService.getUrlStats(shortCode);

    // Build response
    UrlClickResponse response = new UrlClickResponse();
    response.setShortCode(url.getShortCode());
    response.setOriginalUrl(url.getOriginalUrl());
    response.setClickCount(url.getClickCount());
    response.setMessage("Total clicks: " + url.getClickCount());

    return ResponseEntity.ok(response);
  }

  /**
   * Health check endpoint
   * 
   * Path: GET /api/v1/health
   * Response: {"status": "UP"}
   * Used by: Frontend to check if backend is running
   * 
   * Simple endpoint to verify the service is alive
   */
  @GetMapping("/health")
  public ResponseEntity<String> health() {
    return ResponseEntity.ok("{\"status\": \"UP\"}");
  }
}
