# 🌍 TravelSphere Platform

Enterprise Travel Booking & Insurance Platform — 17 Java Spring Boot Microservices + Angular PWA + Agentic AI

## Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                      Angular PWA (Port 4200)                │
└──────────────────────────┬──────────────────────────────────┘
                           │ HTTP/SSE/WebSocket
┌──────────────────────────▼──────────────────────────────────┐
│              API Gateway (Spring Cloud Gateway - 8080)       │
│          JWT Auth · Rate Limiting · Route Routing           │
└──────┬──────┬──────┬──────┬──────┬──────┬──────┬───────────┘
       │      │      │      │      │      │      │
┌──────▼─┐ ┌──▼───┐ ┌▼────┐ ┌▼────┐ ┌▼────┐ ┌▼────┐ ┌──────▼─┐
│ Auth   │ │User  │ │Flight│ │Hotel│ │Car  │ │Insur│ │Payment │
│ 8081   │ │8082  │ │8083  │ │8084 │ │Rent │ │ance │ │ 8089   │
│ JWT+   │ │      │ │      │ │     │ │8086 │ │8087 │ │        │
│ Redis  │ │      │ │      │ │     │ │     │ │     │ │        │
└────────┘ └──────┘ └──────┘ └─────┘ └─────┘ └─────┘ └────────┘
   ... and 10 more microservices ...
┌─────────────────────────────────────────────────────────────┐
│                    Message Broker (Kafka)                    │
└─────────────────────────────────────────────────────────────┘
┌─────────────────────────────────────────────────────────────┐
│                      Databases                               │
│  PostgreSQL 16 · Redis 7 · Qdrant Vector DB · LocalStack S3 │
└─────────────────────────────────────────────────────────────┘
┌─────────────────────────────────────────────────────────────┐
│              AI Layer (Ollama + LangChain4j)                 │
│     llama3.2 (Chat) · nomic-embed-text (Embeddings)         │
└─────────────────────────────────────────────────────────────┘
```

## Tech Stack

| Layer | Technology |
|-------|-----------|
| **Language** | Java 17 (LTS) |
| **Backend** | Spring Boot 3.3, Spring Cloud 2023, Spring AI 1.x |
| **Security** | Spring Security 6, JWT (HS512), BCrypt (12 rounds) |
| **Database** | PostgreSQL 16 (schema-per-service), Redis 7, Qdrant |
| **Messaging** | Apache Kafka 3.7 (20+ topics) |
| **AI/LLM** | Ollama (llama3.2 chat + nomic-embed-text embeddings) |
| **RAG** | LangChain4j + Qdrant vector store + Spring AI |
| **Frontend** | Angular 17+ PWA, Angular Material, NgRx, Leaflet |
| **Infra** | Docker Compose, LocalStack, AWS Free Tier |
| **CI/CD** | GitHub Actions, ghcr.io, AWS EC2 |
| **Monitoring** | Zipkin, Prometheus, Grafana, Kafka UI |
| **Docs** | SpringDoc OpenAPI (Swagger UI) |

## Microservices (17)

| # | Service | Port | Description |
|---|---------|------|-------------|
| 1 | **api-gateway** | 8080 | Spring Cloud Gateway, JWT filter, rate limiting |
| 2 | **service-registry** | 8761 | Netflix Eureka service discovery |
| 3 | **config-server** | 8888 | Spring Cloud Config (Git-backed) |
| 4 | **auth-service** | 8081 | JWT auth, register, login, refresh, Redis tokens |
| 5 | **user-service** | 8082 | Profiles, loyalty points, referrals |
| 6 | **flight-service** | 8083 | Flight search, booking, check-in, seat maps |
| 7 | **hotel-service** | 8084 | Hotel search, rooms, reviews, photos (S3) |
| 8 | **transport-service** | 8085 | Bus/train routes, schedules, PNR |
| 9 | **car-rental-service** | 8086 | Vehicle inventory, add-ons, bookings |
| 10 | **insurance-service** | 8087 | Policy engine, premium calc, claims, PDF |
| 11 | **package-service** | 8088 | Holiday packages, itineraries, group booking |
| 12 | **payment-service** | 8089 | Mock gateway, wallet, promo codes, refunds |
| 13 | **notification-service** | 8091 | Email (Thymeleaf), SMS, WebSocket push |
| 14 | **document-service** | 8092 | PDF generation (iText7), S3 upload/download |
| 15 | **search-service** | 8093 | Full-text search across all domains |
| 16 | **ai-agent-service** | 8094 | Ollama + LangChain4j + MCP + RAG |
| 17 | **admin-service** | 8095 | Inventory, analytics, fraud alerts, support |

## Quick Start (Local)

### Prerequisites
- Docker 24+ & Docker Compose v2
- Java 17 (Temurin)
- Maven 3.9+
- Node.js 20+
- Ollama (for AI features)

### Step 1: Start Infrastructure
```bash
docker compose up -d postgres redis kafka zookeeper qdrant localstack zipkin prometheus grafana kafka-ui mailhog
```

### Step 2: Pull Ollama Models
```bash
ollama pull llama3.2
ollama pull nomic-embed-text
```

### Step 3: Build All Services
```bash
mvn clean package -DskipTests
```

### Step 4: Start Core Services
```bash
docker compose up -d service-registry config-server
sleep 30
docker compose up -d api-gateway auth-service user-service
```

### Step 5: Start Domain Services
```bash
docker compose up -d flight-service hotel-service transport-service car-rental-service insurance-service package-service payment-service
```

### Step 6: Start Support Services
```bash
docker compose up -d notification-service document-service search-service admin-service
```

### Step 7: Start AI Service
```bash
docker compose up -d ai-agent-service
```

### Step 8: Start Angular Frontend
```bash
cd frontend/travelsphere-ui
npm ci
npx ng serve
```

## Access URLs

| Service | URL |
|---------|-----|
| Angular UI | http://localhost:4200 |
| API Gateway | http://localhost:8080 |
| Swagger UI | http://localhost:8080/swagger-ui.html |
| Eureka Dashboard | http://localhost:8761 |
| Kafka UI | http://localhost:8090 |
| Zipkin Tracing | http://localhost:9411 |
| Grafana (admin/admin) | http://localhost:3000 |
| Qdrant Dashboard | http://localhost:6333/dashboard |
| MailHog SMTP UI | http://localhost:8025 |

## API Reference

All endpoints return the standard `ApiResponse` wrapper:
```json
{
  "success": true,
  "data": {},
  "error": null,
  "message": "Operation successful",
  "timestamp": "2024-01-15T10:30:00Z",
  "requestId": "uuid"
}
```

### Auth API
- `POST /api/v1/auth/register` — Register new user
- `POST /api/v1/auth/login` — Login, returns JWT tokens
- `POST /api/v1/auth/refresh` — Refresh access token
- `POST /api/v1/auth/logout` — Revoke tokens
- `GET /api/v1/auth/me` — Current user info

### Flight API
- `GET /api/v1/flights/search` — Search flights
- `GET /api/v1/flights/{id}` — Flight details
- `POST /api/v1/flights/book` — Book a flight
- `PUT /api/v1/flights/cancel/{ref}` — Cancel booking
- `POST /api/v1/flights/check-in/{ref}` — Check in

### Insurance API
- `GET /api/v1/insurance/policies` — List policy types
- `POST /api/v1/insurance/calculate` — Calculate premium
- `POST /api/v1/insurance/purchase` — Purchase policy
- `POST /api/v1/insurance/claims` — File a claim
- `GET /api/v1/insurance/claims/{id}` — Claim status

### Payment API
- `POST /api/v1/payments/initiate` — Initiate payment
- `POST /api/v1/payments/confirm` — Confirm payment
- `GET /api/v1/wallet/balance` — Wallet balance
- `POST /api/v1/wallet/topup` — Add funds

### AI Agent API
- `POST /api/v1/ai/chat` — Chat with AI (SSE streaming)
- `POST /api/v1/ai/plan-trip` — Plan a trip
- `GET /api/v1/ai/recommendations` — Personal recommendations

## AI Agent Examples

```bash
# Chat with TravelSphere AI
curl -X POST http://localhost:8080/api/v1/ai/chat \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"message": "Plan a 7-day trip to Goa under ₹50,000 for 2 people"}'

# Get insurance recommendations
curl -X POST http://localhost:8080/api/v1/ai/insurance-advisor \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"destination": "Switzerland", "duration": 10, "travelerAge": 32}'
```

## AWS Deployment

1. Create EC2 t2.micro with Amazon Linux 2
2. Assign Elastic IP
3. Set up RDS PostgreSQL db.t3.micro
4. Configure GitHub Secrets for CI/CD
5. Push to main → GitHub Actions deploys automatically

## Kafka Topics (20+)

- `ts.flights.booked`, `ts.flights.cancelled`, `ts.flights.checked-in`
- `ts.hotels.booked`, `ts.hotels.cancelled`
- `ts.transport.booked`, `ts.cars.booked`
- `ts.insurance.policy-issued`, `ts.insurance.claim-filed`, `ts.insurance.claim-resolved`
- `ts.packages.booked`, `ts.payments.processed`, `ts.payments.failed`, `ts.payments.refunded`
- `ts.users.registered`, `ts.users.loyalty-updated`
- `ts.documents.generated`, `ts.search.indexed`, `ts.ai.query-logged`
- `ts.notifications.send`, `ts.admin.fraud-alert`

## License

Proprietary — TravelSphere Platform
