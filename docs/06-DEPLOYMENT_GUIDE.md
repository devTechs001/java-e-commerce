# Deployment Guide

## Docker Compose (Dev)

```bash
cd java-e-commerce/docker
docker-compose up -d
```

## Docker Compose (Prod)

```bash
cd java-e-commerce/docker
docker-compose -f docker-compose-prod.yml up -d
```

## Kubernetes

Apply manifests in `kubernetes/` directory:

```bash
kubectl apply -f kubernetes/
```

## Environment Configuration

Production requires:
- `JWT_SECRET` - strong random string
- `SPRING_DATASOURCE_URL` - production DB URL
- `SPRING_DATASOURCE_PASSWORD` - production DB password

## CI/CD

GitHub Actions workflow in `.github/workflows/`:

```yaml
name: Build & Deploy
on:
  push:
    branches: [main]
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - name: Build Backend
        run: cd java-e-commerce && mvn clean package -DskipTests
      - name: Build Frontend
        run: cd frontend && npm ci && npm run build
```
