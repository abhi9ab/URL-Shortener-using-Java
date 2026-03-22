
# URL Shortener Backend (Spring Boot)

A small Spring Boot backend that:
- Creates short URLs
- Expands short URLs back to the original
- Stores mappings in PostgreSQL
- Uses Redis for caching + rate limiting

The app runs on **port 8081** by default.

---

## Tech stack

- Java + Spring Boot
- PostgreSQL (database)
- Redis (cache + rate-limit + hit count)

---

## Prerequisites

- Java 17+ installed
- Docker (recommended for Postgres + Redis)

---

## Start PostgreSQL (Docker)

Run Postgres locally on port 5432:

```bash
docker run --name url-shortener-postgres \
	-e POSTGRES_DB=url_shortener \
	-e POSTGRES_USER=postgres \
	-e POSTGRES_PASSWORD=postgres \
	-p 5432:5432 \
	-d postgres:16
```

Create the schema used by the entity (`schema = "projects"`):

```bash
docker exec -it url-shortener-postgres psql -U postgres -d url_shortener
```

Inside `psql`:

```sql
CREATE SCHEMA IF NOT EXISTS projects;
```

---

## Start Redis (Docker)

```bash
docker run --name url-shortener-redis -p 6379:6379 -d redis:7
```

Quick check:

```bash
docker exec -it url-shortener-redis redis-cli ping
```

---

## Configure the app

Default config is in [src/main/resources/application.properties](src/main/resources/application.properties).

Important values:
- `server.port=8081`
- `spring.datasource.url=jdbc:postgresql://localhost:5432/url_shortener`
- `spring.datasource.username=postgres`
- `spring.datasource.password=postgres`
- `spring.redis.host=localhost`
- `spring.redis.port=6379`

---

## Build and run

```bash
chmod +x mvnw
./mvnw clean test
./mvnw spring-boot:run
```

Once it starts, the API is available at:

`http://localhost:8081/api`

---

## API examples

### 1) Shorten a URL

```bash
curl -X POST http://localhost:8081/api/shorten \
	-H 'Content-Type: application/json' \
	-d '{"url":"https://example.com"}'
```

### 2) Expand a short URL

```bash
curl -X POST http://localhost:8081/api/expand \
	-H 'Content-Type: application/json' \
	-d '{"url":"http://short.est/abc123"}'
```

### 3) List mappings

```bash
curl "http://localhost:8081/api/getAllURLMappings?offset=0&limit=10"
```

---

## Stopping containers

```bash
docker stop url-shortener-postgres url-shortener-redis
```

