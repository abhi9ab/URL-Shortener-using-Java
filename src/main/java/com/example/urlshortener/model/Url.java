package com.example.urlshortener.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * URL Entity - Represents a URL mapping in the database
 * 
 * @Entity: Tells Hibernate that this class maps to a database table
 * @Table: Names the table "urls" in the database
 */
@Entity
@Table(name = "urls")
@Data // Lombok: Auto-generates getters, setters, equals(), hashCode(), toString()
@NoArgsConstructor // Lombok: Generates no-arg constructor (required by JPA)
@AllArgsConstructor // Lombok: Generates constructor with all fields
public class Url {

  /**
   * @Id: Marks this field as the primary key
   * @GeneratedValue: Auto-increment ID (database generates the value)
   *                  Strategy.IDENTITY means the database (H2) auto-generates the
   *                  ID
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * originalUrl: The full, long URL that user wants to shorten
   * Example: "https://www.example.com/api/v1/very-long-endpoint"
   * 
   * @Column(nullable = false): This field cannot be NULL in database
   * @Column(length = 2048): Max length is 2048 characters (standard URL length)
   */
  @Column(nullable = false, length = 2048)
  private String originalUrl;

  /**
   * shortCode: The shortened code
   * Example: "abc123"
   * 
   * @Column(unique = true): Each shortCode must be unique (no duplicates)
   *                This prevents two short codes pointing to different URLs
   * @Column(nullable = false): Cannot be NULL
   * @Column(length = 10): Our short codes are typically 6-10 characters
   */
  @Column(unique = true, nullable = false, length = 10)
  private String shortCode;

  /**
   * createdAt: Timestamp when the URL was shortened
   * Useful for analytics: "When did users create this short URL?"
   */
  @Column(nullable = false)
  private LocalDateTime createdAt;

  /**
   * BONUS: Click count - Track how many times this short URL was used
   * This adds simple analytics to our project
   */
  @Column(nullable = false)
  private Long clickCount = 0L;

  /**
   * lastAccessedAt: Track when the URL was last accessed
   * Useful for analytics: "Which URLs are still being used?"
   */
  private LocalDateTime lastAccessedAt;

  /**
   * Lifecycle hook: Called before entity is saved to database
   * We use this to set createdAt timestamp automatically
   */
  @PrePersist
  public void prePersist() {
    if (this.createdAt == null) {
      this.createdAt = LocalDateTime.now();
    }
    if (this.clickCount == null) {
      this.clickCount = 0L;
    }
  }
}
