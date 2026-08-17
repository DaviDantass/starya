# Stayra

Backend de uma plataforma de aluguel de curta duração. Hóspedes pesquisam e reservam propriedades; anfitriões cadastram anúncios, preços e disponibilidade.

> **Status:** autenticação JWT e cadastro de hóspedes implementados. O próximo fluxo é o cadastro de propriedades e reservas simples, ainda sem concorrência.

## Visão geral

```text
Cliente → Spring Boot API → PostgreSQL
                    └────→ Redis
```

Fluxo planejado:

```text
Pesquisa → Disponibilidade → Hold temporário → Pagamento → Reserva → Estadia → Avaliação
```

### Stack

- Java 21, Spring Boot e Spring Security;
- PostgreSQL + Flyway como fonte de verdade e versionamento do schema;
- Redis para dados temporários com TTL;
- JWT para autenticação stateless;
- Docker Compose para desenvolvimento local;
- Maven, JUnit, Mockito e JaCoCo para testes.

O modelo e as decisões de domínio estão em [TUTORIAL.md](TUTORIAL.md).

## Rodando localmente

Requisitos: Java 21, Docker Desktop e Docker Compose.

```powershell
Copy-Item .env.example .env
docker compose up -d postgres redis
```

Para executar pela IDE, use o perfil `dev`. O Spring importa automaticamente o arquivo `.env`.

Para executar pelo Maven:

```powershell
$env:SPRING_PROFILES_ACTIVE="dev"
.\mvnw.cmd spring-boot:run
```

A API fica em `http://localhost:8080`.

```powershell
docker compose ps
docker compose logs -f api
```

`docker compose down` preserva os dados nos volumes `postgres_data` e `redis_data`. Use `docker compose down --volumes` somente para apagar e recriar os dados locais.

## Configuração

Copie `.env.example` para `.env` e ajuste os valores locais. Nunca versione `.env`.

Para a aplicação executada no IntelliJ, o PostgreSQL usa `localhost`. Dentro do Compose, a API usa os hosts `postgres` e `redis`, configurados automaticamente no `compose.yml`.

## Endpoints atuais

| Método | Endpoint | Acesso |
|---|---|---|
| `POST` | `/users` | público |
| `POST` | `/auth/login` | público |
| `GET` | `/actuator/health` | público |
| demais | — | JWT obrigatório |

O login aceita `username` ou e-mail no campo `identifier`:

```json
{
  "identifier": "david ou david@example.com",
  "password": "sua senha"
}
```

Envie o token nas requisições protegidas:

```http
Authorization: Bearer <token>
```

## Validação e cobertura

```powershell
.\mvnw.cmd test
.\mvnw.cmd verify
```

O `verify` gera o relatório JaCoCo em `target/site/jacoco/index.html` e exige pelo menos 90% de cobertura de linhas da camada de aplicação.

## Roadmap de estudo

Marque um item quando ele estiver implementado, testado e compreendido.

### Base

- [x] Spring Boot, Java 21 e Maven
- [x] PostgreSQL, Redis e Docker Compose
- [x] Migrations iniciais com Flyway
- [x] Entidades principais do domínio
- [x] Cadastro de hóspede em `POST /users`
- [x] BCrypt para senhas
- [x] Testes unitários e regra de cobertura JaCoCo
- [ ] Tratamento global padronizado de erros

### Autenticação

- [x] Login por username ou e-mail
- [x] `UserDetailsService` e `AuthenticationManager`
- [x] Geração, validação e leitura de JWT
- [x] Filtro `Authorization: Bearer <token>`
- [x] Sessão stateless e autorização básica
- [x] Testes de credenciais válidas e inválidas
- [ ] Testes HTTP completos com `MockMvc`
- [ ] Recuperação e troca de senha

### Redis: exercício inicial

- [ ] Confirmar health check do Redis
- [ ] Salvar e ler um valor com TTL
- [ ] Observar a expiração do valor
- [ ] Definir padrão de chaves, como `booking-hold:{propertyId}:{checkIn}:{checkOut}`
- [ ] Criar e testar `BookingHoldRepository`

### Host: propriedades sem concorrência

- [ ] Definir como um usuário se torna `HOST`
- [ ] Criar DTOs, repository e service de propriedade
- [ ] Implementar `POST /properties` para `HOST`
- [ ] Implementar consulta e gerenciamento dos próprios anúncios
- [ ] Validar autorização entre hosts

### Guest: busca e reserva simples

- [ ] Listar propriedades com paginação e filtros
- [ ] Criar `BookingRepository` e `BookingService`
- [ ] Validar `[checkIn, checkOut)` e calcular noites
- [ ] Congelar `totalPrice` e `currency`
- [ ] Implementar `POST /bookings` e `GET /bookings/me`
- [ ] Implementar cancelamento e testes do fluxo

### Evolução

- [ ] Integrar holds do Redis ao checkout
- [ ] Testar e impedir double booking com concorrência
- [ ] Implementar idempotência e pagamentos
- [ ] Processar webhooks e reembolsos
- [ ] Adicionar observabilidade, deploy e frontend

## Regra de implementação

Para cada endpoint: defina o caso de uso, modele request/response, crie a consulta, implemente a service, mantenha o controller fino, configure autorização e teste o caminho feliz e os erros principais.

## Estudos futuros

- concorrência em Java e PostgreSQL;
- consistência transacional, idempotência e outbox;
- arquitetura de plataformas de aluguel;
- operação de aplicações com Docker, secrets e observabilidade.
