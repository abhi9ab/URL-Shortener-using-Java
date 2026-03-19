package com.example.urlshortener.repository;

import com.example.urlshortener.model.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repository Pattern: Database access layer
 * 
 * WHY Spring Data JPA?
 * - We extend JpaRepository<Url, Long>
 * - Spring automatically generates boilerplate code for CRUD operations:
 * - save() - Save/Update an entity
 * - findById() - Find by primary key
 * - findAll() - Fetch all records
 * - delete() - Delete an entity
 * - We only write custom queries we need
 * 
 * JpaRepository<Entity, PrimaryKeyType>
 * - Entity: Url (the object we're persisting)
 * - PrimaryKeyType: Long (the type of @Id field)
 * 
 * @Repository: Spring annotation marking this as a database access layer
 */
@Repository
public interface UrlRepository extends JpaRepository<Url, Long> {

  /**
   * Custom query method: Find URL by shortCode
   * 
   * Spring Data JPA convention:
   * - Method name: findByShortCode
   * - Spring automatically generates SQL: SELECT * FROM urls WHERE short_code = ?
   * - Returns Optional because the shortCode might not exist
   * 
   * Optional<T>: Java way to handle null safely
   * - Optional.of(value): Contains a value
   * - Optional.empty(): No value (instead of null)
   * - Prevents NullPointerException
   */
  Optional<Url> findByShortCode(String shortCode);
}
