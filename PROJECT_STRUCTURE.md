# URL Shortener - Project Structure

```
URL_Shortener_backend/
│
├── 📖 00_START_HERE.md              ⭐ BEGIN HERE - Complete overview & checklist
├── 📖 QUICKSTART.md                 ⭐ 5-minute setup guide
├── 📖 README.md                     ⭐ Full documentation + API reference
├── 📖 INTERVIEW_GUIDE.md            ⭐ How to explain in interviews
├── 📖 ANNOTATIONS_GUIDE.md          ⭐ Learn Spring Boot annotations
├── 📖 TESTING.md                    ⭐ cURL test examples
├── 📖 POSTMAN_GUIDE.md              ⭐ Postman setup & requests
│
├── 📋 pom.xml                       Maven: Dependencies & build config
├── .gitignore                       Git ignore rules
│
└── 📁 src/
    │
    ├── 📁 main/
    │   │
    │   ├── 📁 java/com/example/urlshortener/
    │   │   │
    │   │   ├── ✨ UrlShortenerApplication.java   Main entry point
    │   │   │
    │   │   ├── 📁 model/
    │   │   │   └── 🔵 Url.java                    Database entity
    │   │   │
    │   │   ├── 📁 repository/
    │   │   │   └── 🔵 UrlRepository.java          JPA queries
    │   │   │
    │   │   ├── 📁 service/
    │   │   │   └── 🔵 UrlService.java             Business logic
    │   │   │
    │   │   ├── 📁 controller/
    │   │   │   └── 🔵 UrlController.java          REST endpoints
    │   │   │
    │   │   ├── 📁 dto/
    │   │   │   ├── 🔵 UrlShortenRequest.java      API request DTO
    │   │   │   ├── 🔵 UrlShortenResponse.java     API response DTO
    │   │   │   └── 🔵 UrlClickResponse.java       Stats response DTO
    │   │   │
    │   │   └── 📁 exception/
    │   │       ├── 🔵 UrlNotFoundException.java    Custom exception
    │   │       ├── 🔵 ErrorResponse.java          Error response DTO
    │   │       └── 🔵 GlobalExceptionHandler.java  Global error handler
    │   │
    │   └── 📁 resources/
    │       └── ⚙️  application.properties         Database configuration
    │
    └── 📁 test/
        └── 📁 java/com/example/urlshortener/
            └── 🧪 UrlControllerIntegrationTest.java   Integration tests


KEY:
📖 = Documentation files (read these!)
✨ = Main entry point
🔵 = Java class files
⚙️  = Configuration
🧪 = Test files
```

---

## 📊 File Statistics

| Type          | Count  | Purpose               |
| ------------- | ------ | --------------------- |
| Java classes  | 11     | Complete application  |
| JUnit tests   | 1      | Integration tests     |
| Documentation | 7      | Learning & reference  |
| Config files  | 2      | Maven + Spring config |
| **TOTAL**     | **21** | Complete project      |

Total Java code: **~750 lines** (perfectly sized for learning)

---

## 🏗️ Layered Architecture Map

```
                    API Requests (HTTP)
                          ↑↓
                    ┌─────────────────┐
                    │   Controller    │    Handles HTTP
                    │  UrlController  │    • Parse requests
    Input Validation├─────────────────┤    • Build responses
                    │    Service      │    • Status codes
                    │  UrlService     │    
                    ├─────────────────┤    Core Business Logic
                    │  Repository     │    • Generate codes
                    │  UrlRepository  │    • Validate URLs
                    ├─────────────────┤    • Track clicks
    DTOs / Entities │     Model       │    
                    │      Url        │    Database Mapping
                    ├─────────────────┤    • Fields → Columns
                    │  H2 Database    │    • Constraints
                    │   urls table    │    • Relationships
                    └─────────────────┘
                          ↑↓
                    Data Storage & Retrieval
```

---

## 📈 Request Flow

```
1. USER SENDS REQUEST
   ↓
   POST http://localhost:8080/api/v1/shorten
   { "url": "https://github.com" }

2. SPRING ROUTING
   ↓
   @PostMapping("/shorten") in UrlController

3. CONTROLLER
   ↓
   UrlController.shortenUrl(UrlShortenRequest)
   • Validates request
   • Calls service

4. SERVICE (Business Logic)
   ↓
   UrlService.shortenUrl(String url)
   • Validates URL format
   • Generates short code
   • Calls repository

5. REPOSITORY (Database Access)
   ↓
   urlRepository.save(url)
   • Calls JPA
   • Executes INSERT query
   • Returns saved entity

6. DATABASE
   ↓
   H2 Database executes:
   INSERT INTO urls (original_url, short_code, created_at, click_count)
   VALUES ('https://github.com', 'abc123', NOW(), 0)

7. RESPONSE CHAIN
   ↓
   Service returns Url entity
   ↓
   Controller builds UrlShortenResponse DTO
   ↓
   Spring serializes to JSON
   ↓
   HTTP 201 Created

8. USER GETS RESPONSE
   ↓
   {
     "shortUrl": "http://localhost:8080/abc123",
     "shortCode": "abc123",
     "originalUrl": "https://github.com"
   }
```

---

## 🔄 Redirect Flow

```
1. USER CLICKS SHORT URL
   ↓
   GET http://localhost:8080/abc123

2. CONTROLLER REDIRECTS METHOD
   ↓
   UrlController.directRedirect(shortCode)

3. SERVICE LOOKUPS & UPDATES
   ↓
   UrlService.getUrlByShortCode("abc123")
   • Finds URL in database
   • Increments click_count
   • Updates last_accessed_at
   • Saves changes

4. DATABASE
   ↓
   SELECT * FROM urls WHERE short_code = 'abc123'
   UPDATE urls SET click_count = 2, last_accessed_at = NOW()

5. RESPONSE
   ↓
   HTTP 302 Found
   Location: https://github.com

6. BROWSER
   ↓
   Automatically follows redirect
   ↓
   User lands on https://github.com
```

---

## 📚 Documentation Reading Order

```
Day 1: SETUP (30 min)
├─ 00_START_HERE.md (5 min)        ← You are here!
├─ QUICKSTART.md (10 min)          ← Setup your environment
└─ Test with curl (15 min)         ← Verify it works

Day 2-3: LEARN ARCHITECTURE (2-3 hours)
├─ ANNOTATIONS_GUIDE.md (20 min)   ← Understand @Entity, @Service, etc.
├─ README.md (30 min)              ← Full architecture overview
├─ Code Files:                     ← Read each layer
│  ├─ model/Url.java (20 min)
│  ├─ repository/UrlRepository.java (10 min)
│  ├─ service/UrlService.java (30 min)
│  ├─ controller/UrlController.java (20 min)
│  └─ exception/* (15 min)
└─ POSTMAN_GUIDE.md or TESTING.md ← Test all endpoints

Day 4-5: INTERVIEW PREP (1-2 hours)
├─ INTERVIEW_GUIDE.md (30 min)     ← Learn interview answers
├─ Practice explaining (30 min)    ← Talk through architecture
└─ Modify code (30 min)            ← Add feature, show capability
```

---

## 🎯 Quick Navigation

### "I want to understand the HTTP layer"
→ Read [UrlController.java](src/main/java/com/example/urlshortener/controller/UrlController.java)

### "I want to understand the database"
→ Read [Url.java](src/main/java/com/example/urlshortener/model/Url.java)

### "I want to understand the business logic"
→ Read [UrlService.java](src/main/java/com/example/urlshortener/service/UrlService.java)

### "I want to understand queries"
→ Read [UrlRepository.java](src/main/java/com/example/urlshortener/repository/UrlRepository.java)

### "I want to test the API"
→ Read [TESTING.md](TESTING.md) or [POSTMAN_GUIDE.md](POSTMAN_GUIDE.md)

### "I'm in an interview and need to explain"
→ Read [INTERVIEW_GUIDE.md](INTERVIEW_GUIDE.md)

### "I want to understand annotations"
→ Read [ANNOTATIONS_GUIDE.md](ANNOTATIONS_GUIDE.md)

---

## ⚡ Command Cheat Sheet

```bash
# Build project (first time, or after major changes)
mvn clean install

# Run application (starts server on port 8080)
mvn spring-boot:run

# Run tests
mvn test

# Build JAR (for production)
mvn clean package
# Then run: java -jar target/urlshortener-1.0.0.jar

# View logs while running
# (Just watch the output in the same terminal)

# Test health check
curl http://localhost:8080/api/v1/health

# Shorten a URL
curl -X POST http://localhost:8080/api/v1/shorten \
  -H "Content-Type: application/json" \
  -d '{"url": "https://github.com"}'

# Redirect (replace abc123 with your shortCode)
curl -L http://localhost:8080/abc123

# Get statistics
curl http://localhost:8080/api/v1/stats/abc123

# View H2 Console (in browser)
http://localhost:8080/h2-console

# List all Java classes
find . -name "*.java" -type f

# View file count
find . -name "*.java" | wc -l
```

---

## 🧠 Key Concepts at a Glance

| Concept                      | Why?                        | Example                                        |
| ---------------------------- | --------------------------- | ---------------------------------------------- |
| **Layered Architecture**     | Separation of concerns      | Controller handles HTTP, Service handles logic |
| **Dependency Injection**     | Loose coupling, testability | `@Autowired private UrlService service`        |
| **DTOs**                     | Decouple API from database  | `UrlShortenResponse` vs `Url` entity           |
| **REST**                     | Standard HTTP architecture  | POST /shorten (create), GET /{id} (read)       |
| **JPA**                      | Avoid writing SQL           | `urlRepository.findByShortCode()`              |
| **UNIQUE Constraint**        | Data integrity              | No two URLs with same short code               |
| **Collision Detection**      | Robustness                  | Check if code exists before using              |
| **Base62 Encoding**          | Efficiency                  | 6 chars = 56 billion possibilities             |
| **Global Exception Handler** | Consistency                 | All errors return same format                  |
| **Timestamps**               | Analytics                   | Track when URLs created/accessed               |

---

## ✅ Success Criteria

You're ready when:

- [ ] ✅ Can run `mvn spring-boot:run` without errors
- [ ] ✅ Can access `/api/v1/health` endpoint
- [ ] ✅ Can test all 4 API endpoints
- [ ] ✅ Can view H2 database console
- [ ] ✅ Understand why we use layers
- [ ] ✅ Can explain UNIQUE constraint
- [ ] ✅ Can trace a request end-to-end
- [ ] ✅ Can explain to someone else
- [ ] ✅ Can answer interview questions
- [ ] ✅ Can extend with new features

---

## 🎓 What You've Learned

This project teaches **real production patterns**:

✅ Entity-Service-Repository pattern
✅ Dependency injection
✅ REST API design
✅ Database entity mapping
✅ Exception handling
✅ Data validation
✅ Short code generation algorithm
✅ HTTP status codes
✅ Request/response DTOs
✅ Integration testing

---

## 🚀 Next Level

Want to extend this project?

- **Authentication**: Add Spring Security + JWT
- **Caching**: Use Redis for popular URLs
- **Persistence**: Switch to PostgreSQL
- **Rate Limiting**: Prevent abuse
- **Analytics**: Dashboard showing trends
- **QR Codes**: Generate QR from short URL
- **API Documentation**: Swagger/OpenAPI
- **Docker**: Containerize the app
- **Cloud Deploy**: AWS/Azure/Heroku

---

## 💡 Pro Tips

1. **Learn through doing**: Don't just read code, modify it
2. **Use debugger**: Step through requests in IDE
3. **Check H2 console**: Verify data in database
4. **Read comments**: Every complex part is explained
5. **Practice explaining**: Teach someone else
6. **Extend features**: Add your own ideas

---

## ❓ Help!

**"I'm lost"**
→ Start with [QUICKSTART.md](QUICKSTART.md)

**"Code doesn't make sense"**
→ Read [ANNOTATIONS_GUIDE.md](ANNOTATIONS_GUIDE.md)

**"Can't run the project"**
→ Check [QUICKSTART.md](QUICKSTART.md) troubleshooting

**"Interview is tomorrow"**
→ Speed-read [INTERVIEW_GUIDE.md](INTERVIEW_GUIDE.md)

**"I want to learn more"**
→ Read [README.md](README.md) completely

---

## 📞 Your Next Step

👉 **Open [00_START_HERE.md](00_START_HERE.md) in your terminal:**

```bash
cat /home/abhi9ab/Developer/URL_Shortener_backend/00_START_HERE.md
```

Or open in VS Code:
```bash
code /home/abhi9ab/Developer/URL_Shortener_backend
```

---

**You're all set! Have fun building and learning! 🚀**

