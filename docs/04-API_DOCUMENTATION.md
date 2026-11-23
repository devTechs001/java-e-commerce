# API Documentation

Swagger UI is available at `http://localhost:8080/swagger-ui.html` when the backend is running.

OpenAPI spec: `http://localhost:8080/v3/api-docs`

## Authentication Endpoints

| Method | Endpoint | Body | Response |
|--------|----------|------|----------|
| POST | `/auth/register` | `{email, password, firstName, lastName}` | `AuthResponse` |
| POST | `/auth/login` | `{email, password}` | `AuthResponse` |

## Product Endpoints

| Method | Endpoint | Query Params | Response |
|--------|----------|--------------|----------|
| GET | `/products` | — | `List<ProductResponse>` |
| GET | `/products/{id}` | — | `ProductResponse` |
| GET | `/products/search` | `keyword` | `List<ProductResponse>` |

## Cart Endpoints

| Method | Endpoint | Body | Response |
|--------|----------|------|----------|
| GET | `/cart` | — | `CartResponse` |
| POST | `/cart/add` | `{productId, quantity}` | `CartResponse` |
| DELETE | `/cart/{itemId}` | — | `CartResponse` |

## Order Endpoints

| Method | Endpoint | Response |
|--------|----------|----------|
| GET | `/orders` | `List<OrderResponse>` |
| GET | `/orders/{id}` | `OrderResponse` |

## Response Format

All responses use `ApiResponse`:

```json
{
  "success": true,
  "message": "Operation completed",
  "data": { ... }
}
```

All requests requiring auth include `Authorization: Bearer <token>` header.
