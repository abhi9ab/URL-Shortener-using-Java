# URL Shortener - Postman Collection

## Import Into Postman

1. Open **Postman**
2. Click **Import** (top-left)
3. Select **Link** tab
4. Paste: `https://www.postman.com/collections/YOUR_COLLECTION_ID` (if hosted)

**OR** create requests manually as shown below:

---

## Request 1: Health Check

```
GET http://localhost:8080/api/v1/health
```

No body needed.

**Expected Response (200 OK):**
```json
{"status": "UP"}
```

---

## Request 2: Shorten URL

```
POST http://localhost:8080/api/v1/shorten
Content-Type: application/json

{
  "url": "https://www.wikipedia.org/wiki/URL_shortening"
}
```

**Headers:**
| Key          | Value            |
| ------------ | ---------------- |
| Content-Type | application/json |

**Body (JSON):**
```json
{
  "url": "https://www.wikipedia.org/wiki/URL_shortening"
}
```

**Expected Response (201 Created):**
```json
{
  "shortUrl": "http://localhost:8080/abc123",
  "shortCode": "abc123",
  "originalUrl": "https://www.wikipedia.org/wiki/URL_shortening"
}
```

**💡 Save the `shortCode` value! You'll use it in next requests.**

---

## Request 3: Redirect to Original URL

```
GET http://localhost:8080/api/v1/abc123
```

Replace `abc123` with the `shortCode` from Request 2.

**Expected Response (302 Found):**
The server redirects to the original URL automatically.

Check the **Headers** tab:
```
Location: https://www.wikipedia.org/wiki/URL_shortening
```

**Postman Tip**: Enable "Follow redirects" in:
Settings → General → Follow Redirects (ON)

Then you'll see the actual Wikipedia page content.

---

## Request 4: Get Click Statistics

```
GET http://localhost:8080/api/v1/stats/abc123
```

Replace `abc123` with your `shortCode`.

**Expected Response (200 OK):**
```json
{
  "shortCode": "abc123",
  "originalUrl": "https://www.wikipedia.org/wiki/URL_shortening",
  "clickCount": 1,
  "message": "Total clicks: 1"
}
```

**Tip**: Call Request 3 multiple times, then call this again. The `clickCount` will increase! 📈

---

## Request 5: Alternative Redirect (Longer Path)

```
GET http://localhost:8080/api/v1/redirect/abc123
```

This is the longer version of Request 3. Both work the same way.

---

## Error Test Cases

### Error 1: Invalid URL (No Protocol)

```
POST http://localhost:8080/api/v1/shorten
Content-Type: application/json

{
  "url": "github.com"
}
```

**Expected Response (400 Bad Request):**
```json
{
  "status": 400,
  "message": "URL must start with http:// or https://",
  "timestamp": "2024-01-15T10:30:45"
}
```

---

### Error 2: Non-existent Short Code

```
GET http://localhost:8080/api/v1/abc123notfound
```

**Expected Response (404 Not Found):**
```json
{
  "status": 404,
  "message": "Short code 'abc123notfound' not found",
  "timestamp": "2024-01-15T10:30:45"
}
```

---

### Error 3: Empty URL

```
POST http://localhost:8080/api/v1/shorten
Content-Type: application/json

{
  "url": ""
}
```

**Expected Response (400 Bad Request):**
```json
{
  "status": 400,
  "message": "URL cannot be empty",
  "timestamp": "2024-01-15T10:30:45"
}
```

---

## Tips for Using This in Postman

### 1. Use Environment Variables

Save common URLs as variables:

1. Click **Environments** (top-left)
2. Create new: `Local Dev`
3. Add variable:
   ```
   Key: base_url
   Value: http://localhost:8080
   ```
4. Use in requests:
   ```
   {{base_url}}/api/v1/health
   ```

### 2. Use Pre-request Scripts

Auto-generate test data before each request:

In **Tests** tab:
```javascript
// Save shortCode for next request
var shortCode = pm.response.json().shortCode;
pm.environment.set("lastShortCode", shortCode);
```

Then use in next request:
```
GET {{base_url}}/{{lastShortCode}}
```

### 3. Create a Collection

1. Click **New** → **Collection** → URL Shortener
2. Add all requests to this collection
3. Share collection with team

### 4. Run Tests

With all requests in a Collection:

1. Click **Runner** (left sidebar)
2. Select your Collection
3. Click **Run**
4. Watch all tests run automatically!

---

## Tips for Explaining to Interviewers

"I designed a REST API with:
- **POST /shorten**: Create short URLs
- **GET /{shortCode}**: Redirect to original
- **GET /stats/{shortCode}**: View analytics

I use **DTOs** for request/response to decouple API from database entities.

**Error handling** is centralized with `@ControllerAdvice`, so all errors return consistent JSON format."

---

**Happy testing!** 🧪

