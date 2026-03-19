package com.example.urlshortener;

import com.example.urlshortener.controller.UrlController;
import com.example.urlshortener.dto.UrlShortenRequest;
import com.example.urlshortener.dto.UrlShortenResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

/**
 * Integration Tests for URL Shortener API
 * 
 * @SpringBootTest: Loads entire Spring application context for integration
 *                  testing
 * @AutoConfigureMockMvc: Provides MockMvc for testing HTTP requests without
 *                        starting actual server
 * 
 *                        Why test?
 *                        - Automated verification that endpoints work correctly
 *                        - Catch bugs before production
 *                        - Refactor with confidence (tests catch regressions)
 *                        - Document how API should work
 */
@SpringBootTest
@AutoConfigureMockMvc
public class UrlControllerIntegrationTest {

  /**
   * MockMvc: Simulate HTTP requests without starting full server
   * Much faster than integration tests with real server
   */
  @Autowired
  private MockMvc mockMvc;

  /**
   * Test 1: Health Check Endpoint
   * 
   * Verify: GET /api/v1/health returns 200 OK
   */
  @Test
  public void testHealthEndpoint() throws Exception {
    mockMvc.perform(get("/api/v1/health"))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("UP")));
  }

  /**
   * Test 2: Shorten URL - Happy Path
   * 
   * Verify: POST /api/v1/shorten creates short URL successfully
   */
  @Test
  public void testShortenUrl_Success() throws Exception {
    // Arrange: Prepare request JSON
    String requestBody = "{\"url\": \"https://example.com/very-long-url\"}";

    // Act: Send POST request
    MvcResult result = mockMvc.perform(post("/api/v1/shorten")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody))
        // Assert: Verify response
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.shortUrl", notNullValue()))
        .andExpect(jsonPath("$.shortCode", notNullValue()))
        .andExpect(jsonPath("$.originalUrl", is("https://example.com/very-long-url")))
        .andReturn();

    // Extract shortCode for next test
    String responseBody = result.getResponse().getContentAsString();
    System.out.println("Response: " + responseBody);
  }

  /**
   * Test 3: Shorten URL - Invalid URL (no protocol)
   * 
   * Verify: POST /api/v1/shorten rejects URLs without http/https
   */
  @Test
  public void testShortenUrl_InvalidUrl() throws Exception {
    String requestBody = "{\"url\": \"example.com\"}"; // Missing protocol

    mockMvc.perform(post("/api/v1/shorten")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status", is(400)))
        .andExpect(jsonPath("$.message", containsString("http")));
  }

  /**
   * Test 4: Redirect with Non-existent Short Code
   * 
   * Verify: GET /api/v1/{shortCode} returns 404 for invalid code
   */
  @Test
  public void testRedirect_NotFound() throws Exception {
    mockMvc.perform(get("/api/v1/nonexistent123"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status", is(404)))
        .andExpect(jsonPath("$.message", containsString("not found")));
  }

  /**
   * Test 5: Shorten URL - Empty URL
   * 
   * Verify: Validation catches empty URLs
   */
  @Test
  public void testShortenUrl_EmptyUrl() throws Exception {
    String requestBody = "{\"url\": \"\"}";

    mockMvc.perform(post("/api/v1/shorten")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody))
        .andExpect(status().isBadRequest());
  }

  /**
   * Test 6: Statistics Endpoint
   * 
   * Verify: Get stats for non-existent URL returns 404
   */
  @Test
  public void testGetStats_NotFound() throws Exception {
    mockMvc.perform(get("/api/v1/stats/nonexistent123"))
        .andExpect(status().isNotFound());
  }
}
