# E-Commerce Platform - Overview

Enterprise E-Commerce Platform built with **Spring Boot 3.2.2** backend and **React + Vite** frontend.

## Architecture

| Layer | Technology |
|-------|-----------|
| Backend | Spring Boot 3.2.2, Java 17, Spring Web, Spring Data JPA |
| Database | PostgreSQL (prod), H2 (dev) |
| Security | Spring Security, JWT (jjwt 0.12.3), BCrypt |
| API Docs | SpringDoc OpenAPI (Swagger UI) |
| Frontend | React 18, Vite, React Router, Axios, Tailwind CSS |
| Build | Maven, npm |

## Features

- User registration & authentication (JWT)
- Product catalog (list, search, detail)
- Shopping cart (add, remove, update quantity)
- Order placement & checkout
- Order history
- User profile management
- Dark/light theme toggle
- Responsive layout

## Project Structure

```
java-e-commerce/           # Spring Boot backend (Maven)
frontend/                  # React + Vite frontend
docker/                    # Docker Compose, Dockerfiles, nginx config
docs/                      # Project documentation
scripts/                   # Build & maintenance scripts
kubernetes/                # K8s manifests
terraform/                 # Infrastructure as Code
monitoring/                # Prometheus, Grafana, ELK
```

## Quick Start

```bash
# Development - Backend
cd java-e-commerce && mvn spring-boot:run

# Development - Frontend
cd frontend && npm run dev

# Production (Docker)
./start-ecommerce.sh prod
```

## Endpoints

| Method | Path | Description | Auth |
|--------|------|-------------|------|
| POST | /auth/register | Register user | No |
| POST | /auth/login | Login | No |
| GET | /products | List products | No |
| GET | /products/{id} | Product detail | No |
| GET | /cart | View cart | Yes |
| POST | /cart/add | Add to cart | Yes |
| DELETE | /cart/{itemId} | Remove from cart | Yes |
| GET | /orders | User orders | Yes |
| GET | /orders/{id} | Order detail | Yes |

Backend runs on port **8080**, frontend on port **5173** (dev) or **80** (prod).
