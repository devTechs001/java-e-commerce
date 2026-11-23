# Architecture

## System Components

### Backend (Spring Boot)
- **controllers/** — REST controllers for Auth, Product, Category, Cart, Order
- **services/** — Business logic layer
- **repository/** — JPA repositories
- **model/** — Entities, DTOs, enums
- **security/** — JWT token provider, authentication filter
- **config/** — Security config, OpenAPI config, DataInitializer
- **exception/** — Custom exceptions & global handler

### Frontend (React + Vite)
- **pages/** — Home, Login, Register, Products, ProductDetail, Cart, Checkout, Orders, Profile
- **components/** — Layout, Navbar, Footer
- **context/** — AuthContext, CartContext, ThemeContext
- **services/** — Axios API client

### Data Flow

1. Client sends REST request via Axios to `/api/*`
2. Spring Security validates JWT (if present)
3. Controller delegates to Service
4. Service persists via JPA Repository to PostgreSQL
5. Response returned as `ApiResponse`

### Database Schema (Key Entities)

User → Cart → CartItem ← Product  
User → Order → OrderItem ← Product  
Category → Product  
Address → User
