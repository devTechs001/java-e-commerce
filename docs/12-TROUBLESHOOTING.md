# Troubleshooting

- Backend fails to start: check DB connectivity and application.yml
- JWT errors: check JWT_SECRET and token format
- CORS issues: check spring.web.cors allowed-origins
- Frontend API errors: check Vite proxy config for /api

## Common Logs

- `org.springframework.web: DEBUG` in application.yml
- Hibernate SQL logging enabled via show-sql: true
