# 🔗 URL Shortener Backend

A clean, beginner-friendly URL Shortener service built with Spring Boot. Perfect for learning REST APIs, OOP, and database design. **Easy to understand and explain in interviews.**

---

## 📚 Project Overview

This project demonstrates how to build a production-ready backend service using:
- **Spring Boot**: Modern Java framework for building REST APIs
- **Spring Data JPA**: Object-Relational Mapping (ORM) for database operations
- **H2 Database**: In-memory database for quick development
- **Lombok**: Reduces boilerplate code
- **Layered Architecture**: Clean separation of concerns

### What This Project Teaches

✅ **OOP Concepts**: Entities, Services, Repositories (dependency injection)
✅ **REST API Design**: Request/response DTOs, HTTP methods, status codes
✅ **Database Design**: JPA entities, relationships, UNIQUE constraints
✅ **Exception Handling**: Custom exceptions, centralized error handling
✅ **Software Architecture**: Clean layered design for scalability

---

## 🏗️ Folder Structure Explained

```
src/main/java/com/example/urlshortener/
│
├── model/              # Database entities (data models)
│   └── Url.java        # URL entity: represents a URL shortening record
│
├── repository/         # Database access layer
│   └── UrlRepository.java  # JPA queries to fetch URLs
│
├── service/            # Business logic layer
│   └── UrlService.java # Core business logic (shorten, redirect)
│
├── controller/         # REST API endpoints
│   └── UrlController.java  # HTTP request handlers
│
├── dto/                # Data Transfer Objects (API request/response)
│   └── UrlShortenRequest.java
│   └── UrlShortenResponse.java
│   └── UrlClickResponse.java
│
├── exception/          # Custom exceptions & error handlers
│   ├── UrlNotFoundException.java
│   ├── ErrorResponse.java
│   └── GlobalExceptionHandler.java
│
└── UrlShortenerApplication.java  # Main entry point
```

### Layer Explanation

| Layer          | Responsibility    | Example                             |
| -------------- | ----------------- | ----------------------------------- |
| **Model**      | Database entities | `Url` class maps to `urls` table    |
| **Repository** | Database queries  | `findByShortCode()` method          |
| **Service**    | Business logic    | Generate short code, validate URL   |
| **Controller** | HTTP endpoints    | `POST /shorten`, `GET /{shortCode}` |
| **DTO**        | API contracts     | Request/response objects            |

---

## 🚀 Getting Started

### Prerequisites

- **Java 17+**: [Download JDK](https://www.oracle.com/java/technologies/downloads/)
- **Maven 3.6+**: [Download Maven](https://maven.apache.org/download.cgi)
- **Git** (optional)

### Step 1: Clone or Download Project

```bash
cd /home/abhi9ab/Developer/URL_Shortener_backend
```

### Step 2: Build Project

Compiles Java code and downloads dependencies.

```bash
mvn clean install
```

**What happens:**
- Downloads all dependencies from pom.xml
- Compiles Java source files
- Creates `target/` folder with compiled classes

### Step 3: Run Application

```bash
mvn spring-boot:run
```

**Output should show:**
```
Started UrlShortenerApplication in X.XXX seconds
Server running on: http://localhost:8080
```

✅ **Your backend is now running!**

### Step 4: Access H2 Console (Optional)

View database in browser:
```
http://localhost:8080/h2-console
```

Login with:
- **JDBC URL**: `jdbc:h2:mem:urlshortenerdb`
- **Username**: `sa`
- **Password**: (leave blank)

Click "Connect" to view the `urls` table and all data.

---

## 📊 Database Schema

```sql
CREATE TABLE urls (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    original_url    VARCHAR(2048) NOT NULL,          -- Full URL
    short_code      VARCHAR(10) NOT NULL UNIQUE,     -- Must be unique!
    created_at      TIMESTAMP NOT NULL,
    click_count     BIGINT DEFAULT 0,
    last_accessed_at TIMESTAMP
);
```

**Key Points:**
- `short_code` is UNIQUE: Prevents two different URLs having same short code
- `click_count`: Tracks how many times the link was used
- `created_at`: Timestamp for analytics

---

## 🔌 API Endpoints

### 1️⃣ Health Check

**Purpose**: Verify backend is running

```http
GET /api/v1/health
```

**Response** (200 OK):
```json
{"status": "UP"}
```

---

### 2️⃣ Shorten URL

**Purpose**: Convert long URL to short URL

```http
POST /api/v1/shorten
Content-Type: application/json

{
  "url": "https://stackoverflow.com/questions/1234567/how-to-validate-date-yyyy-mm-dd-format"
}
```

**Response** (201 Created):
```json
{
  "shortUrl": "http://localhost:8080/abc123",
  "shortCode": "abc123",
  "originalUrl": "https://stackoverflow.com/questions/1234567/how-to-validate-date-yyyy-mm-dd-format"
}
```

**How It Works:**
1. Service validates URL (must start with http/https)
2. Generates random 6-character code: `abc123`
3. Checks if `abc123` already exists (collision detection)
4. Saves mapping: `abc123` → `https://stackoverflow.com/...`
5. Returns short URL to client

---

### 3️⃣ Redirect to Original URL

**Purpose**: Redirect short link to original URL

```http
GET /api/v1/{shortCode}

OR

GET /api/v1/redirect/{shortCode}
```

**Example** (Replace `abc123` with your actual short code):
```bash
GET http://localhost:8080/api/v1/abc123
```

**Response** (302 Found):
```
Location: https://stackoverflow.com/questions/1234567/...
```

**How It Works:**
1. Look up short code in database
2. **BONUS**: Increment `click_count` (+1)
3. Update `last_accessed_at` timestamp
4. Send HTTP 302 redirect to original URL

**Browser Action**: When you visit `http://localhost:8080/api/v1/abc123`, browser automatically redirects to the original URL. ✨

---

### 4️⃣ Get Click Statistics

**Purpose**: See how many times a URL was accessed

```http
GET /api/v1/stats/abc123
```

**Response** (200 OK):
```json
{
  "shortCode": "abc123",
  "originalUrl": "https://stackoverflow.com/...",
  "clickCount": 5,
  "message": "Total clicks: 5"
}
```

**Use Case**: Analytics, tracking popular links

---

## 💻 Testing with cURL

### Test 1: Health Check

```bash
curl http://localhost:8080/api/v1/health
```

### Test 2: Shorten URL

```bash
curl -X POST http://localhost:8080/api/v1/shorten \
  -H "Content-Type: application/json" \
  -d '{"url": "https://github.com/torvalds/linux"}'
```

**Example Response:**
```json
{
  "shortUrl": "http://localhost:8080/xyz789",
  "shortCode": "xyz789",
  "originalUrl": "https://github.com/torvalds/linux"
}
```

### Test 3: Copy the shortCode and Redirect

```bash
# Using the shortCode from previous response
curl -L http://localhost:8080/api/v1/xyz789
```

The `-L` flag tells curl to follow redirects. You'll see the original GitHub page content.

### Test 4: Get Statistics

```bash
curl http://localhost:8080/api/v1/stats/xyz789
```

**Response:**
```json
{
  "shortCode": "xyz789",
  "originalUrl": "https://github.com/torvalds/linux",
  "clickCount": 1,
  "message": "Total clicks: 1"
}
```

---

## 🧪 Testing with Postman

### Import Collection

1. Open **Postman**
2. Click **Import** → **Paste Raw Text**
3. Copy/paste this cURL command set, or manually create requests as shown below

### Request 1: Shorten URL

```
POST http://localhost:8080/api/v1/shorten
Header: Content-Type: application/json

Body:
{
  "url": "https://example.com/very-very-long-url-that-needs-shortening"
}
```

### Request 2: Redirect

```
GET http://localhost:8080/abc123
```

(Copy the `shortCode` from Request 1 response)

### Request 3: Get Stats

```
GET http://localhost:8080/api/v1/stats/abc123
```

---

## 🔍 Code Walkthrough (Interview Explanation)

### How to Explain the Architecture

**Q: "Walk me through the layers of your application."**

**Answer:**
- **Controller Layer**: Receives HTTP requests
  - User sends: `POST /api/v1/shorten` with URL
  - Returns JSON response with short URL

- **Service Layer**: Business logic
  - Validates URL format
  - Generates random 6-character code
  - Checks for collisions (no duplicate short codes)
  - Saves to database

- **Repository Layer**: Database access
  - Spring Data JPA generates query automatically
  - `findByShortCode()` → SQL: `SELECT * FROM urls WHERE short_code = ?`

- **Model Layer**: Database entity
  - `Url` class matches `urls` table
  - Fields: `id`, `originalUrl`, `shortCode`, `createdAt`, `clickCount`

---

### How to Explain Short Code Generation

**Q: "How do you generate the short code? What if there's a collision?"**

**Answer:**
1. **Character Set**: Base62 (0-9, a-z, A-Z) = 62 possible characters
2. **Generation**: Random 6-character code → 62^6 = ~56 billion possible codes
3. **Collision Check**: Query database before saving:
   ```
   if (urlRepository.findByShortCode(shortCode).isEmpty()) {
       // Safe to use
   } else {
       // Generate another code
   }
   ```
4. **Why UNIQUE constraint?**: Database enforces no duplicates as backup safety

---

### How to Explain the Redirect

**Q: "How does the redirect work? What's the journey?"**

**Answer:**
1. User clicks short link: `http://localhost:8080/abc123`
2. Browser sends `GET /abc123` to server
3. Controller calls `UrlService.getUrlByShortCode("abc123")`
4. Service **finds** the URL in database
5. Service **increments** `clickCount` (bonus feature)
6. Service **updates** `lastAccessedAt` timestamp
7. Controller sends HTTP 302 response:
   ```
   Location: https://original-long-url.com/...
   ```
8. Browser automatically redirects to original URL

---

### How to Explain DTOs

**Q: "Why use DTOs instead of exposing the Url entity directly?"**

**Answer:**
1. **Encapsulation**: Hide internal entity structure from API clients
2. **Security**: Don't expose fields we don't want shared
3. **Flexibility**: Change database schema without breaking API contract
4. **Example**:
   - Entity has: `id`, `clickCount`, `createdAt`, `lastAccessedAt`
   - DTO only exposes: `shortUrl`, `shortCode`, `originalUrl`
   - If we add internal fields to entity, API stays the same

---

### How to Explain Exception Handling

**Q: "How do you handle errors?"**

**Answer:**
1. **Custom Exception**: `UrlNotFoundException` for missing short codes
2. **Global Handler**: `@ControllerAdvice` catches exceptions globally
3. **Consistent Response**: All errors return JSON:
   ```json
   {
     "status": 404,
     "message": "Short code 'xyz' not found",
     "timestamp": "2024-01-15T10:30:45"
   }
   ```
4. **Benefits**: No try-catch in controllers, consistent error format

---

## 🎯 Key Concepts Explained

### OOP - Object-Oriented Programming

- **Encapsulation**: Each class has one responsibility
  - `Url`: Represents data
  - `UrlService`: Business logic
  - `UrlController`: HTTP handling
- **Inheritance**: Controllers inherit from Spring base classes
- **Polymorphism**: Repository uses Spring's generic interface

### REST API Best Practices

- **Stateless**: Each request is independent, no session state
- **HTTP Methods**: POST (create), GET (read), not GET for state changes
- **Status Codes**: 201 (Created), 200 (OK), 404 (Not Found)
- **URLs**: Resource-based (`/api/v1/shorten`), not action-based

### Database Design

- **Primary Key**: Auto-increment `id` for each record
- **UNIQUE Constraint**: `shortCode` must be unique
- **Timestamps**: `createdAt` for analytics
- **Normalization**: Each field serves a purpose (no duplicates)

### Spring Boot Features

- **Dependency Injection**: Automatic object creation and injection
- **Auto-Configuration**: Detects classpath deps and configures automatically
- **Embedded Server**: Tomcat runs inside JAR (no separate server)
- **AOT vs JIT**: Spring handles runtime optimizations

---

## 🎓 Interview Tips

### What You Can Say About This Project

✅ "I designed a **layered architecture** with clear separation of concerns"

✅ "I use **Spring Data JPA** for ORM, so I don't write SQL manually"

✅ "I implemented **centralized exception handling** with `@ControllerAdvice`"

✅ "I use **DTOs** to decouple API contract from database entities"

✅ "I track **analytics** by incrementing click count on each redirect"

✅ "I added **collision detection** for short code generation"

### Questions You Might Face

| Question                                                | Your Answer                                                 |
| ------------------------------------------------------- | ----------------------------------------------------------- |
| "How do you prevent duplicate short codes?"             | UNIQUE constraint + `findByShortCode()` check before saving |
| "What's the difference between Controller and Service?" | Controller handles HTTP, Service handles business logic     |
| "Why use repositories?"                                 | Abstraction layer, easier testing with mocks                |
| "How does Dependency Injection work?"                   | Spring manages object lifecycle, injects via `@Autowired`   |
| "What if short code collision happens?"                 | Database constraint prevents it; code retries generation    |

---

## 🔧 Optional Enhancements

These are ideas to extend the project:

### 1. Expiration (TTL)

Add `expiresAt` field, delete old records:
```java
@Column
private LocalDateTime expiresAt;

// Scheduled task
@Scheduled(fixedRate = 3600000) // Every hour
public void deleteExpiredUrls() {
    urlRepository.deleteByExpiresAtBefore(LocalDateTime.now());
}
```

### 2. Custom Short Code

Allow users to specify their own short code:
```json
{
  "url": "https://example.com",
  "customCode": "mycode"
}
```

### 3. QR Code Generation

Generate QR code that contains the short URL:
```java
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;

QRCodeWriter writer = new QRCodeWriter();
BitMatrix matrix = writer.encode(shortUrl, BarcodeFormat.QR_CODE, 200, 200);
```

### 4. Rate Limiting

Prevent abuse by limiting requests:
```java
@Configuration
public class RateLimitConfig {
    // Limit 100 requests per minute per IP
}
```

### 5. PostgreSQL

Swap H2 for PostgreSQL (production database):
```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <version>42.6.0</version>
</dependency>
```

---

## 📖 Learning Resources

- **Spring Boot Official**: https://spring.io/projects/spring-boot
- **Spring Data JPA**: https://spring.io/projects/spring-data-jpa
- **REST API Design**: https://restfulapi.net/
- **H2 Database**: http://www.h2database.com/

---

## 🤝 Contributing

This project is for learning. Feel free to:
- Add more features
- Optimize code
- Add unit tests
- Deploy to cloud (AWS, Azure, etc.)

---

## 📝 License

MIT License - Use freely for learning and projects

---

## ❓ FAQ

**Q: Can I use PostgreSQL instead of H2?**
A: Yes! Add PostgreSQL dependency and update `application.properties` with connection details.

**Q: How do I deploy this to production?**
A: Build JAR with `mvn clean package`, then run `java -jar target/urlshortener-1.0.0.jar`

**Q: Can I add authentication?**
A: Yes, use Spring Security to add JWT tokens or API keys.

**Q: What's the maximum short code length?**
A: Currently 6 characters (62^6 = ~56 billion possibilities). Adjust `SHORT_CODE_LENGTH` in `UrlService.java`

**Q: How do I test this without Postman?**
A: Use curl commands shown above, or integrate with frontend React/Vue app.

---

## 🚀 Next Steps

1. ✅ Run the application
2. ✅ Test endpoints with curl or Postman
3. ✅ View H2 database console
4. ✅ Read code comments to understand each part
5. ✅ Try modifying code (add new fields, endpoints, etc.)
6. ✅ Explain architecture to someone (or in interview!)

---

**Good luck! Ask questions, break things, learn. This is how you grow as a developer.** 🚀

