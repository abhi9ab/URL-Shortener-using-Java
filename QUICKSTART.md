# 🚀 URL Shortener - Quick Start Guide

## ⚡ 5-Minute Setup

### Step 1: Build the Project
```bash
cd /home/abhi9ab/Developer/URL_Shortener_backend
mvn clean install
```
This downloads all dependencies and compiles the code.

✅ Success looks like:
```
BUILD SUCCESS
Total time: X minutes Y seconds
```

### Step 2: Run the Application
```bash
mvn spring-boot:run
```

✅ Success looks like:
```
Tomcat started on port(s): 8080 (http)
Started UrlShortenerApplication in X.XXX seconds
```

### Step 3: Test It Works
```bash
curl http://localhost:8080/api/v1/health
```

✅ Response:
```json
{"status": "UP"}
```

**🎉 You're done! Backend is running.**

---

## 📚 Understanding the Code

### Read in This Order

1. **[ANNOTATIONS_GUIDE.md](ANNOTATIONS_GUIDE.md)** ← Start here!
   - Learn what each `@` symbol means
   - 10 minutes read
   - Makes everything else click

2. **Model Layer**: [model/Url.java](src/main/java/com/example/urlshortener/model/Url.java)
   - Database entity
   - How fields map to table
   - Understand `@Entity`, `@Column`, `@PrePersist`

3. **Repository Layer**: [repository/UrlRepository.java](src/main/java/com/example/urlshortener/repository/UrlRepository.java)
   - Database queries
   - How Spring generates SQL from method names
   - Understand `@Repository`, `Optional`

4. **Service Layer**: [service/UrlService.java](src/main/java/com/example/urlshortener/service/UrlService.java)
   - Business logic
   - URL validation
   - Short code generation algorithm
   - Click tracking

5. **Controller Layer**: [controller/UrlController.java](src/main/java/com/example/urlshortener/controller/UrlController.java)
   - REST API endpoints
   - Request/response handling
   - HTTP status codes

6. **Exception Handling**: [exception/](src/main/java/com/example/urlshortener/exception/)
   - Custom exceptions
   - Global error handler
   - Consistent error responses

7. **DTOs**: [dto/](src/main/java/com/example/urlshortener/dto/)
   - Request/response objects
   - Why we don't expose entities directly

---

## 🧪 Test the API

### Option 1: Using cURL (Recommended for Learning)
```bash
# 1. Test health
curl http://localhost:8080/api/v1/health

# 2. Shorten a URL
curl -X POST http://localhost:8080/api/v1/shorten \
  -H "Content-Type: application/json" \
  -d '{"url": "https://github.com"}'

# 3. Use the shortCode from response
curl -L http://localhost:8080/abc123

# 4. Check stats
curl http://localhost:8080/api/v1/stats/abc123
```

See [TESTING.md](TESTING.md) for more curl examples.

### Option 2: Using Postman
See [POSTMAN_GUIDE.md](POSTMAN_GUIDE.md) for complete guide.

### Option 3: Using Browser
1. Health check: http://localhost:8080/api/v1/health
2. H2 Console: http://localhost:8080/h2-console

---

## 📖 Documentation Files

| File                                         | Purpose                                   | Read Time |
| -------------------------------------------- | ----------------------------------------- | --------- |
| [README.md](README.md)                       | Complete overview, architecture, API docs | 20 min    |
| [INTERVIEW_GUIDE.md](INTERVIEW_GUIDE.md)     | How to explain to interviewers            | 15 min    |
| [ANNOTATIONS_GUIDE.md](ANNOTATIONS_GUIDE.md) | Spring Boot annotations explained         | 10 min    |
| [TESTING.md](TESTING.md)                     | cURL testing examples                     | 5 min     |
| [POSTMAN_GUIDE.md](POSTMAN_GUIDE.md)         | Postman setup and requests                | 5 min     |

---

## 📂 Project Structure

```
URL_Shortener_backend/
│
├── pom.xml                          ← Dependencies config
├── README.md                        ← Main documentation
├── INTERVIEW_GUIDE.md               ← Interview prep
├── ANNOTATIONS_GUIDE.md             ← Spring annotations
├── TESTING.md                       ← cURL tests
├── POSTMAN_GUIDE.md                 ← Postman guide
├── .gitignore                       ← Git ignore rules
│
├── src/main/
│   ├── java/com/example/urlshortener/
│   │   │
│   │   ├── UrlShortenerApplication.java  ← Entry point
│   │   │
│   │   ├── model/
│   │   │   └── Url.java              ← Database entity
│   │   │
│   │   ├── repository/
│   │   │   └── UrlRepository.java    ← Database queries
│   │   │
│   │   ├── service/
│   │   │   └── UrlService.java       ← Business logic
│   │   │
│   │   ├── controller/
│   │   │   └── UrlController.java    ← REST endpoints
│   │   │
│   │   ├── dto/                       ← API request/response objects
│   │   │   ├── UrlShortenRequest.java
│   │   │   ├── UrlShortenResponse.java
│   │   │   └── UrlClickResponse.java
│   │   │
│   │   └── exception/                 ← Error handling
│   │       ├── UrlNotFoundException.java
│   │       ├── ErrorResponse.java
│   │       └── GlobalExceptionHandler.java
│   │
│   └── resources/
│       └── application.properties     ← Database config
│
└── src/test/java/
    └── com/example/urlshortener/
        └── UrlControllerIntegrationTest.java  ← Unit tests
```

---

## 🎓 Learning Path

### Day 1: Run & Test
- [ ] Run the app (`mvn spring-boot:run`)
- [ ] Test endpoints with curl
- [ ] View H2 console
- [ ] Understand: "It works!"

### Day 2: Understand Architecture
- [ ] Read [ANNOTATIONS_GUIDE.md](ANNOTATIONS_GUIDE.md)
- [ ] Read [model/Url.java](src/main/java/com/example/urlshortener/model/Url.java) line by line
- [ ] Read [repository/UrlRepository.java](src/main/java/com/example/urlshortener/repository/UrlRepository.java)
- [ ] Understand: "This is the data layer"

### Day 3: Business Logic
- [ ] Read [service/UrlService.java](src/main/java/com/example/urlshortener/service/UrlService.java)
- [ ] Trace through `shortenUrl()` method
- [ ] Understand short code generation
- [ ] Understand: "This is where logic happens"

### Day 4: API Endpoints
- [ ] Read [controller/UrlController.java](src/main/java/com/example/urlshortener/controller/UrlController.java)
- [ ] Understand each endpoint
- [ ] Test each endpoint
- [ ] Understand: "This is the HTTP interface"

### Day 5: Error Handling & DTOs
- [ ] Read exception handling
- [ ] Read DTOs
- [ ] Test error cases (invalid URL, not found, etc.)
- [ ] Understand: "Robust error handling"

### Day 6: Interview Prep
- [ ] Read [INTERVIEW_GUIDE.md](INTERVIEW_GUIDE.md)
- [ ] Practice explaining architecture
- [ ] Answer sample questions out loud
- [ ] Understand: "I can explain this!"

---

## 💡 Key Concepts Covered

### Object-Oriented Programming (OOP)
- ✅ **Encapsulation**: Each layer has one responsibility
- ✅ **Inheritance**: Spring base classes
- ✅ **Polymorphism**: Repository interface + implementation

### REST API Design
- ✅ **HTTP Methods**: POST (create), GET (retrieve)
- ✅ **Status Codes**: 201, 200, 404, 400
- ✅ **Resource-based URLs**: `/api/v1/shorten`, not `/shortenUrl`
- ✅ **DTOs**: Separate API contract from database

### Database Design
- ✅ **Entity Mapping**: Classes to tables
- ✅ **Constraints**: Primary keys, UNIQUE, NOT NULL
- ✅ **Relationships**: One-to-many (future learning)

### Spring Boot Framework
- ✅ **Dependency Injection**: `@Autowired`
- ✅ **Auto-Configuration**: H2 auto-configured
- ✅ **Annotations**: `@Entity`, `@Service`, `@Repository`, etc.
- ✅ **Exception Handling**: `@ControllerAdvice`

### Data Structures
- ✅ **Collision Detection**: Hash checking
- ✅ **Base62 Encoding**: Efficient code generation
- ✅ **Optional**: Null-safe object handling

---

## 🔧 Common Commands

```bash
# Build project
mvn clean install

# Run application
mvn spring-boot:run

# Run tests
mvn test

# Build JAR (production)
mvn clean package
# Then run: java -jar target/urlshortener-1.0.0.jar

# View Java version
java -version

# View Maven version
mvn -version

# Kill process on port 8080
lsof -i :8080
sudo kill -9 <PID>
```

---

## ❓ Common Questions

**Q: The app won't start**
A: Check if port 8080 is in use. Run `lsof -i :8080` to find process, then `kill -9 <PID>`

**Q: I get "Command not found: mvn"**
A: Install Maven or add to PATH. Check: `mvn -version`

**Q: Where's my data after restart?**
A: H2 in-memory database resets. To persist, edit `application.properties` to use file-based H2.

**Q: Can I use PostgreSQL instead?**
A: Yes! Add dependency, update `application.properties` with connection details.

**Q: How do I deploy this?**
A: Run `mvn package`, then upload `target/urlshortener-1.0.0.jar` to server, run `java -jar urlshortener-1.0.0.jar`

**Q: Can I add more features?**
A: Absolutely! Ideas: authentication, rate limiting, QR codes, custom codes, API analytics, etc.

---

## 📚 Next Steps After Learning

1. **Add Unit Tests**: Expand `UrlControllerIntegrationTest.java`
2. **Add Authentication**: Spring Security with JWT tokens
3. **Switch to PostgreSQL**: Production database
4. **Deploy**: AWS, Azure, Heroku, DigitalOcean, etc.
5. **Add Frontend**: React/Vue to consume API
6. **Add Caching**: Redis for popular URLs
7. **Add Rate Limiting**: Prevent abuse
8. **Add Analytics**: Dashboard showing trends

---

## 🎯 Interview Talking Points

**Architecture:**
"Layered design with Controller, Service, Repository separating concerns"

**Database:**
"JPA entity with UNIQUE constraint on short_code, auto-incrementing ID"

**Short Code:**
"6-character Base62 code with collision detection"

**Analytics:**
"Track click count and last access time for each URL"

**Error Handling:**
"Global exception handler for consistent error responses"

**Testing:**
"Integration tests with MockMvc, easy to mock dependencies"

---

## 🚀 You're Ready!

This project demonstrates:
- ✅ Clean architecture
- ✅ Best practices
- ✅ Production-ready code
- ✅ Interview-friendly explanations

**Go build something awesome!** 🎉

For questions, refer to the guides above or read the code comments.

---

**Happy learning!** 📚

