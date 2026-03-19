# Spring Boot Annotations & Concepts Quick Reference

Use this guide while reading the code to understand what each annotation does.

---

## 🏛️ Class-Level Annotations

### @Entity
```java
@Entity
public class Url { }
```
**Meaning**: "Tell Hibernate/JPA: this class maps to a database table"

**What it does**:
- JPA scans this class and creates table automatically
- Each field becomes a column
- Constructor must be no-arg (JPA needs it)

**Example**: `Url` class → `urls` table

---

### @Table
```java
@Entity
@Table(name = "urls")
public class Url { }
```
**Meaning**: "Table name is 'urls' (not 'url')"

**What it does**:
- Specifies table name in database
- If omitted, uses class name in lowercase

**Example**: Url class → urls table ✅ (not 'url' table)

---

### @RestController
```java
@RestController
public class UrlController { }
```
**Meaning**: "This class handles REST API requests"

**What it does**:
- Combines `@Controller` + `@ResponseBody`
- All methods return JSON (not HTML templates)
- Spring automatically deserializes requests, serializes responses

---

### @Service
```java
@Service
public class UrlService { }
```
**Meaning**: "This is a service component (business logic layer)"

**What it does**:
- Spring creates a singleton instance of this class
- Makes it available for dependency injection
- Indicates to developers: "Business logic lives here"

---

### @Repository
```java
@Repository
public interface UrlRepository extends JpaRepository<Url, Long> { }
```
**Meaning**: "This is a database access component"

**What it does**:
- Spring detects database exceptions, converts to Spring exceptions
- Makes available for dependency injection
- Indicates: "Database queries here"

---

### @ControllerAdvice
```java
@ControllerAdvice
public class GlobalExceptionHandler { }
```
**Meaning**: "Handle exceptions globally across all controllers"

**What it does**:
- Catches specific exceptions thrown anywhere
- Returns consistent error responses
- Centralized error handling (no try-catch in controllers)

---

## 🖇️ Method-Level Annotations

### @PostMapping
```java
@PostMapping("/shorten")
public ResponseEntity<UrlShortenResponse> shortenUrl(...) { }
```
**Meaning**: "Handle HTTP POST requests to /shorten"

**HTTP Semantics**: POST = "Create a new resource"

**URL**: `POST /api/v1/shorten` (v1 added by @RequestMapping)

---

### @GetMapping
```java
@GetMapping("/{shortCode}")
public void redirect(@PathVariable String shortCode) { }
```
**Meaning**: "Handle HTTP GET requests to /{shortCode}"

**HTTP Semantics**: GET = "Retrieve a resource"

**URL**: `GET /api/v1/{shortCode}`

---

### @RequestMapping
```java
@RestController
@RequestMapping("/api/v1")
public class UrlController { }
```
**Meaning**: "All endpoints in this controller start with /api/v1"

**Example**:
```
@PostMapping("/shorten") inside this controller
→ Full URL: POST /api/v1/shorten
```

---

### @RequestBody
```java
@PostMapping("/shorten")
public ResponseEntity<...> shortenUrl(
    @RequestBody UrlShortenRequest request) { }
```
**Meaning**: "HTTP request body contains JSON, convert to Java object"

**Process**:
```
Client sends:  { "url": "https://..." }
           ↓ (deserialize)
UrlShortenRequest object with url field set
```

---

### @PathVariable
```java
@GetMapping("/{shortCode}")
public void redirect(@PathVariable String shortCode) { }
```
**Meaning**: "Extract {shortCode} from URL path and set as parameter"

**Example**:
```
Request: GET /api/v1/abc123
shortCode parameter = "abc123"
```

---

### @ExceptionHandler
```java
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UrlNotFoundException.class)
    public ResponseEntity<ErrorResponse> handle(...) { }
}
```
**Meaning**: "This method handles UrlNotFoundException"

**When triggered**:
```
UrlNotFoundException thrown anywhere
         ↓
GlobalExceptionHandler.handle() called
         ↓
Returns consistent error response
```

---

## 💉 Dependency Injection Annotations

### @Autowired
```java
@Service
public class UrlService {
    @Autowired
    private UrlRepository urlRepository;
}
```
**Meaning**: "Spring, inject UrlRepository instance here"

**What happens**:
1. Spring sees `@Autowired` on field
2. Finds bean of type UrlRepository
3. Creates instance (or uses existing singleton)
4. Assigns to field
5. You can use `urlRepository` in methods

**Benefits**:
- No `new UrlRepository()` needed
- Easy to mock in tests
- Loose coupling

---

## 📊 Field-Level Annotations

### @Id
```java
@Entity
public class Url {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
```
**Meaning**: "This field is the primary key"

**What it does**:
- Uniquely identifies each row
- Database creates unique index
- Cannot be null

---

### @GeneratedValue
```java
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
```
**Meaning**: "Database auto-generates ID value"

**Strategy**:
- `IDENTITY`: Database generates (e.g., AUTO_INCREMENT in H2)
- `SEQUENCE`: Use database sequence
- `TABLE`: Use auxiliary table for IDs
- `UUID`: Generate UUID

**Example**: Insert without specifying id
```java
url.setId(null); // or omit
urlRepository.save(url); // id auto-assigned
```

---

### @Column
```java
@Entity
public class Url {
    @Column(nullable = false, length = 2048)
    private String originalUrl;
    
    @Column(unique = true, nullable = false)
    private String shortCode;
}
```
**Meaning**: "Specify column constraints"

**Options**:
- `nullable = false`: NOT NULL constraint
- `unique = true`: UNIQUE constraint (no duplicates)
- `length = 2048`: VARCHAR(2048)
- `name = "custom_name"`: Column name (if different from field)

**Examples**:
```sql
-- originalUrl field becomes:
original_url VARCHAR(2048) NOT NULL

-- shortCode field becomes:
short_code VARCHAR(10) NOT NULL UNIQUE
```

---

### @PrePersist
```java
@Entity
public class Url {
    @Column
    private LocalDateTime createdAt;
    
    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }
}
```
**Meaning**: "Run this method before saving to database"

**When called**: Right before INSERT

**Use case**: Set timestamps automatically

**Example Flow**:
```
1. Create Url object (createdAt is null)
2. Call urlRepository.save(url)
3. Spring calls prePersist()
4. prePersist() sets createdAt to now
5. INSERT happens with timestamp
6. Return saved object with id
```

---

### Lombok Annotations

#### @Data
```java
@Entity
@Data
public class Url { }
```
**Meaning**: "Auto-generate getters, setters, equals(), hashCode(), toString()"

**What generated**:
```
getters:       getId(), getOriginalUrl(), getShortCode()
setters:       setId(), setOriginalUrl(), setShortCode()
equals():      compares all fields
hashCode():    suitable for HashMap/HashSet
toString():    Url(id=1, originalUrl=..., shortCode=...)
```

---

#### @NoArgsConstructor
```java
@Entity
@NoArgsConstructor
public class Url { }
```
**Meaning**: "Generate no-argument constructor"

**Generated**:
```java
public Url() {
}
```

**Why needed**: JPA requires no-arg constructor

---

#### @AllArgsConstructor
```java
@Entity
@AllArgsConstructor
public class Url { }
```
**Meaning**: "Generate constructor with all fields"

**Generated**:
```java
public Url(Long id, String originalUrl, String shortCode, 
           LocalDateTime createdAt, Long clickCount, 
           LocalDateTime lastAccessedAt) {
    this.id = id;
    this.originalUrl = originalUrl;
    // ... etc
}
```

**Use case**: Testing, DTOs

---

## 🔄 HTTP Status Codes (Quick Reference)

| Code                 | Meaning                | Use Case                            |
| -------------------- | ---------------------- | ----------------------------------- |
| **200 OK**           | Request successful     | GET succeeded, data returned        |
| **201 Created**      | Resource created       | POST created new entity             |
| **302 Found**        | Redirect               | Temporary redirect (our short code) |
| **400 Bad Request**  | Invalid input          | Missing/wrong field, invalid URL    |
| **404 Not Found**    | Resource doesn't exist | Short code not in database          |
| **500 Server Error** | Unexpected error       | Database connection failed          |

---

## 🔗 Relationship Between Annotations

```
@Entity + @Table
        ↓
        Url class = urls table

@RestController + @RequestMapping("/api/v1")
        ↓
        UrlController handles /api/v1/* requests

@PostMapping("/shorten") + @RequestBody
        ↓
        POST /api/v1/shorten expects JSON body

@Service + @Autowired
        ↓
        Spring injects UrlService instance

@Repository
        ↓
        UrlRepository extends JpaRepository
        ↓
        Spring generates save(), findById(), findByShortCode() methods

@ControllerAdvice + @ExceptionHandler
        ↓
        Catch exceptions globally, return consistent error JSON
```

---

## 📝 Example: Full Request Flow With Annotations

```
1. Client sends: POST /api/v1/shorten { "url": "https://..." }

2. Spring sees @PostMapping("/shorten")
   → Routes to UrlController.shortenUrl()

3. @RequestBody annotation
   → Deserializes JSON → UrlShortenRequest object

4. @Service on UrlService
   → Spring finds UrlService bean

5. @Autowired on UrlRepository
   → Spring injects repository instance

6. @Repository on UrlRepository
   → Spring generates CRUD methods

7. urlRepository.save(url)
   → Spring Data JPA issue INSERT query

8. @Entity + @PrePersist
   → Calls prePersist() to set timestamp
   → Calls @GeneratedValue to assign id

9. Returns saved Url object to service

10. Service returns UrlShortenResponse (DTO)

11. Spring calls ObjectMapper
    → Serializes Java object to JSON

12. @RestController
    → Returns JSON as HTTP response body

13. Client receives: 201 Created with JSON response
```

---

## 🎯 Key Takeaways

| Concept              | Annotation                                     | Purpose                   |
| -------------------- | ---------------------------------------------- | ------------------------- |
| Entity Mapping       | @Entity, @Table                                | Link class to table       |
| REST Routing         | @RestController, @RequestMapping               | HTTP request handling     |
| HTTP Methods         | @PostMapping, @GetMapping, etc.                | Specify HTTP verb         |
| Request Data         | @RequestBody, @PathVariable                    | Extract data from request |
| Business Layer       | @Service                                       | Marks service class       |
| Database Layer       | @Repository                                    | Marks repository class    |
| Dependency Injection | @Autowired                                     | Spring injects instances  |
| Error Handling       | @ControllerAdvice, @ExceptionHandler           | Global exception handling |
| DB Constraints       | @Column, @Id, @GeneratedValue                  | Map to DDL                |
| Lifecycle Hooks      | @PrePersist                                    | Run before database op    |
| Boilerplate          | @Data, @NoArgsConstructor, @AllArgsConstructor | Lombok code generation    |

---

## 💡 Practice

Try to:
1. Explain what each annotation does from memory
2. Predict what query Spring generates from method name: `findByShortCodeAndClickCountGreaterThan`
3. Write a new entity class with proper annotations
4. Modify an HTTP response code without looking at this guide

Good luck! 🚀

