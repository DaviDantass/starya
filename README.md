# Stayra

API para uma plataforma de locação de curta duração, projetada para manter consistência de disponibilidade, segurança de reservas e rastreabilidade de pagamentos.

> Status: em desenvolvimento. O domínio e a infraestrutura local estão em construção; os casos de uso e endpoints ainda serão implementados.

## Visão técnica

```text
Cliente → Spring Boot API → PostgreSQL
                    └────→ Redis
```

- **Java 21 + Spring Boot:** API e regras de aplicação;
- **PostgreSQL:** fonte de verdade para usuários, imóveis, reservas e pagamentos;
- **Redis:** holds temporários com expiração;
- **Flyway:** versionamento imutável do schema;
- **Docker Compose:** ambiente local reproduzível;
- **Actuator:** health checks e informações operacionais.

## Domínio atual

- usuários hóspedes e anfitriões;
- anúncios, fotos, comodidades e preços por data;
- disponibilidade, bloqueios e proteção contra reservas sobrepostas;
- reservas com preço e moeda congelados;
- pagamentos idempotentes, tentativas e reembolsos;
- valores monetários com `BigDecimal` e `NUMERIC`.

## Executar localmente

Requisito: Docker Desktop com Docker Compose.

```powershell
Copy-Item .env.example .env
docker compose up --build -d
docker compose ps
```

A API fica disponível em `http://localhost:8080`. Para acompanhar os logs:

```powershell
docker compose logs -f api
```

Para encerrar preservando os dados:

```powershell
docker compose down
```

Os dados permanecem nos volumes nomeados do PostgreSQL e Redis. `docker compose down --volumes` remove esses dados e deve ser usado somente para reinicializar o ambiente.

## Desenvolvimento

Para executar a infraestrutura no Docker e a aplicação pela IDE:

```powershell
docker compose up -d postgres redis
$env:SPRING_PROFILES_ACTIVE="dev"
.\mvnw.cmd spring-boot:run
```

## Validação

```powershell
.\mvnw.cmd test
docker compose up --build -d
docker compose ps
```

## Próximas etapas

- repositories, services e endpoints REST;
- autenticação e autorização;
- integração com gateway de pagamento e webhooks;
- testes de integração e concorrência com Testcontainers;
- outbox, observabilidade e pipeline CI/CD.
