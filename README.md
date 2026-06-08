# 🌍 TravelSphere Platform

Enterprise Travel Booking & Insurance Platform — 18 Java Spring Boot Microservices + Angular PWA + Agentic AI

[![GitHub](https://img.shields.io/badge/GitHub-Anilg1997%2Fai--travelsphere--platform-blue?logo=github)](https://github.com/Anilg1997/ai-travelsphere-platform)

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
   ... and 11 more microservices ...
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

## Project Structure

```
travelsphere-platform/
├── backend/                    # 18 Java Spring Boot microservices
│   ├── service-registry/       # Netflix Eureka (port 8761)
│   ├── config-server/          # Spring Cloud Config (port 8888)
│   ├── api-gateway/            # Spring Cloud Gateway (port 8080)
│   ├── auth-service/           # JWT auth (port 8081)
│   ├── user-service/           # User profiles (port 8082)
│   ├── flight-service/         # Flight booking (port 8083)
│   ├── hotel-service/          # Hotel booking (port 8084)
│   ├── transport-service/      # Bus/train (port 8085)
│   ├── car-rental-service/     # Car rental (port 8086)
│   ├── insurance-service/      # Insurance (port 8087)
│   ├── package-service/        # Holiday packages (port 8088)
│   ├── payment-service/        # Payments (port 8089)
│   ├── notification-service/   # Email/SMS (port 8091)
│   ├── document-service/       # PDF generation (port 8092)
│   ├── search-service/         # Full-text search (port 8093)
│   ├── ai-agent-service/       # AI agent (port 8094)
│   ├── admin-service/          # Admin panel (port 8095)
│   └── common-lib/             # Shared library
├── frontend/
│   └── travelsphere-ui/        # Angular PWA app
├── config-repo/                # Spring Cloud Config files
├── infra/                      # Docker/infra configs
├── .env                        # Local environment variables
├── docker-compose.yml          # All services orchestration
└── pom.xml                     # Root Maven POM
```

## Quick Start (Local)

### Prerequisites
- Docker 24+ & Docker Compose v2
- Java 17 or 21 (Temurin JDK)
- Maven 3.9+
- Node.js 20+
- Ollama (for AI features)

### Step 1: Configure Environment
```bash
# Copy the .env file (already provided with local defaults)
cp .env.example .env
```

### Step 2: Start Infrastructure
```bash
docker compose up -d postgres redis kafka zookeeper qdrant localstack zipkin prometheus grafana kafka-ui mailhog
```

### Step 3: Pull Ollama Models
```bash
ollama pull llama3.2
ollama pull nomic-embed-text
```

### Step 4: Build All Services
```bash
# Make sure JAVA_HOME points to JDK 17 or 21
export JAVA_HOME=/path/to/jdk-21
mvn clean package -DskipTests
```

### Step 5: Start Core Services
```bash
docker compose up -d service-registry config-server
sleep 30
docker compose up -d api-gateway auth-service user-service
```

### Step 6: Start Domain Services
```bash
docker compose up -d flight-service hotel-service transport-service car-rental-service insurance-service package-service payment-service
```

### Step 7: Start Support Services
```bash
docker compose up -d notification-service document-service search-service admin-service
```

### Step 8: Start AI Service
```bash
docker compose up -d ai-agent-service
```

### Step 9: Start Angular Frontend
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

> **Note:** You'll need an AWS account for deployment. Set up these GitHub Secrets when ready:
> - `AWS_ACCESS_KEY_ID`, `AWS_SECRET_ACCESS_KEY`, `AWS_REGION`
> - `EC2_HOST`, `EC2_USERNAME`, `EC2_SSH_KEY`

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
