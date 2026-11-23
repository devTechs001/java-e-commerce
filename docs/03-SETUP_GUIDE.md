# Setup Guide

## Prerequisites

- Java 17+
- Node.js 20+ & npm
- Maven 3.9+
- Docker & Docker Compose (optional)

## Backend Setup

```bash
cd java-e-commerce

# Run tests
mvn test

# Build JAR
mvn clean package -DskipTests

# Run (dev)
mvn spring-boot:run
```

Backend starts on `http://localhost:8080/api`.

## Frontend Setup

```bash
cd frontend

# Install dependencies
npm install

# Run dev server
npm run dev
# Runs on http://localhost:5173

# Production build
npm run build
```

## Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| JWT_SECRET | defaultSecretKey... | JWT signing secret |
| JWT_EXPIRATION | 86400000 | Token expiry in ms |
| SPRING_DATASOURCE_URL | jdbc:postgresql://localhost:5432/ecommerce | DB URL |
| SPRING_DATASOURCE_USERNAME | postgres | DB user |
| SPRING_DATASOURCE_PASSWORD | postgres | DB password |

## Docker (Full Stack)

```bash
cd java-e-commerce
./start-ecommerce.sh      # dev mode
./start-ecommerce.sh prod # production mode
```

Services:
- Frontend: http://localhost
- Backend API: http://localhost:8080/api
- PostgreSQL: localhost:5432
