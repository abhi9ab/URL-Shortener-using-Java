# URL Shortener - Quick Test Commands

Use these commands to test the API. Make sure the application is running first!

## Prerequisites

```bash
# Start the application
mvn spring-boot:run

# In another terminal, run these commands:
```

---

## Test 1: Health Check

Verify the backend is running.

```bash
curl http://localhost:8080/api/v1/health
```

Expected output:
```
{"status": "UP"}
```

---

## Test 2: Shorten Your First URL

Create a short URL for a website.

```bash
curl -X POST http://localhost:8080/api/v1/shorten \
  -H "Content-Type: application/json" \
  -d '{"url": "https://www.wikipedia.org/wiki/URL_shortening"}'
```

Expected output:
```json
{
  "shortUrl": "http://localhost:8080/abc123",
  "shortCode": "abc123",
  "originalUrl": "https://www.wikipedia.org/wiki/URL_shortening"
}
```

**Copy the `shortCode` from the response for next tests!**

---

## Test 3: Test the Redirect

Replace `abc123` with your short code from Test 2.

```bash
# Follow the redirect
curl -L http://localhost:8080/api/v1/abc123
```

This will redirect to the original URL. The `-L` flag tells curl to follow redirects.

Shorter version (without redirect):
```bash
curl -i http://localhost:8080/api/v1/abc123
```

Look for:
```
HTTP/1.1 302 Found
Location: https://www.wikipedia.org/wiki/URL_shortening
```

---

## Test 4: Check Click Statistics

See how many times the URL was accessed.

```bash
# Replace abc123 with your short code
curl http://localhost:8080/api/v1/stats/abc123
```

Expected output:
```json
{
  "shortCode": "abc123",
  "originalUrl": "https://www.wikipedia.org/wiki/URL_shortening",
  "clickCount": 1,
  "message": "Total clicks: 1"
}
```

The click count should increase each time you access `/abc123`!

---

## Test 5: Try with Different URLs

```bash
# Test with GitHub
curl -X POST http://localhost:8080/api/v1/shorten \
  -H "Content-Type: application/json" \
  -d '{"url": "https://github.com/torvalds/linux"}'

# Test with a very long URL
curl -X POST http://localhost:8080/api/v1/shorten \
  -H "Content-Type: application/json" \
  -d '{"url": "https://www.example.com/api/v1/users/12345/profile/settings/notifications/email/preferences/advanced?theme=dark&language=en&timezone=UTC"}'
```

---

## Test 6: Error Handling

### Test Invalid URL (missing protocol)

```bash
curl -X POST http://localhost:8080/api/v1/shorten \
  -H "Content-Type: application/json" \
  -d '{"url": "github.com"}'
```

Expected error:
```json
{
  "status": 400,
  "message": "URL must start with http:// or https://",
  "timestamp": "2024-01-15T10:30:45"
}
```

### Test Non-existent Short Code

```bash
curl http://localhost:8080/nonexistent123
```

Expected error:
```json
{
  "status": 404,
  "message": "Short code 'nonexistent123' not found",
  "timestamp": "2024-01-15T10:30:45"
}
```

---

## Test 7: View Database with H2 Console

Open browser and go to:
```
http://localhost:8080/h2-console
```

Login:
- **JDBC URL**: `jdbc:h2:mem:urlshortenerdb`
- **Username**: `sa`
- **Password**: (leave blank)

Click "Connect", then run SQL:
```sql
SELECT * FROM urls;
```

See all your shortened URLs! 📊

---

## Test 8: Multiple Consecutive Requests

Chain multiple operations together.

```bash
# 1. Create first short URL
echo "Creating first URL..."
SHORT1=$(curl -s -X POST http://localhost:8080/api/v1/shorten \
  -H "Content-Type: application/json" \
  -d '{"url": "https://stackoverflow.com"}' | grep -o '"shortCode":"[^"]*"' | cut -d'"' -f4)
echo "Short code 1: $SHORT1"

# 2. Create second short URL
echo "Creating second URL..."
SHORT2=$(curl -s -X POST http://localhost:8080/api/v1/shorten \
  -H "Content-Type: application/json" \
  -d '{"url": "https://github.com"}' | grep -o '"shortCode":"[^"]*"' | cut -d'"' -f4)
echo "Short code 2: $SHORT2"

# 3. Click each URL multiple times
for i in {1..3}; do
  echo "Click $i on $SHORT1"
  curl -s -L http://localhost:8080/$SHORT1 > /dev/null
done

for i in {1..5}; do
  echo "Click $i on $SHORT2"
  curl -s -L http://localhost:8080/$SHORT2 > /dev/null
done

# 4. Check stats
echo "Stats for first URL:"
curl -s http://localhost:8080/api/v1/stats/$SHORT1 | grep clickCount

echo "Stats for second URL:"
curl -s http://localhost:8080/api/v1/stats/$SHORT2 | grep clickCount
```

---

## Bonus: JSON Pretty Print

Add ` | jq` to any JSON response to format prettily (requires jq):

```bash
# Install jq (on macOS)
brew install jq

# Install jq (on Ubuntu/Debian)
sudo apt-get install jq

# Pretty print the response
curl http://localhost:8080/api/v1/health | jq
```

---

## Troubleshooting

### Application won't start

```bash
# Check if port 8080 is in use
lsof -i :8080

# Kill process using port 8080
sudo kill -9 <PID>
```

### Curl command not found (Windows)

Install curl or use PowerShell equivalent:

```powershell
# PowerShell version
$uri = 'http://localhost:8080/api/v1/health'
Invoke-WebRequest -Uri $uri | ConvertTo-Json
```

### H2 sees incorrect data

H2 is in-memory, so data resets when server restarts. To persist:

Edit `src/main/resources/application.properties`:
```properties
# Change from:
spring.datasource.url=jdbc:h2:mem:urlshortenerdb

# To:
spring.datasource.url=jdbc:h2:file:./data/urlshortenerdb
```

---

**Happy testing! 🎉**

