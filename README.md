# Expense Tracker

A Spring Boot REST API for managing users, expense categories, and expenses. The API uses MySQL for persistence and JWT bearer tokens for authentication.

## Requirements

- Java 21
- MySQL
- Docker and Docker Compose (optional)

## Configuration

Create a MySQL database:

```sql
CREATE DATABASE expense_tracker;
```

Copy `.env.example` to `.env` in the project root and set your MySQL credentials and a private JWT secret of at least 32 characters:

```properties
DB_URL=jdbc:mysql://localhost:3306/expense_tracker
DB_USERNAME=root
DB_PASSWORD=your-mysql-password
JWT_SECRET=replace-with-a-private-random-secret-of-at-least-32-characters
JWT_EXPIRATION_SECONDS=3600
SERVER_PORT=8082
```

The application imports `.env` automatically when started from the project root. Database schema updates are managed by Hibernate; use an existing database carefully and do not change the schema strategy to `create` or `create-drop`.

## Run

On Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

On macOS or Linux:

```bash
./mvnw spring-boot:run
```

The API runs at `http://localhost:8082` by default. To run the tests:

```powershell
.\mvnw.cmd test
```

## Docker

With `.env` configured, start the application container with:

```bash
docker compose up --build
```

Compose connects to the configured existing MySQL database (by default on the host at `host.docker.internal:3306`); it does not start or manage a database container. The app is published on port `8083` by default, separate from the local app's default port `8082`, so both can run at once. Set `DOCKER_HOST_PORT` in `.env` to change the Docker host port. Stop the container with `docker compose down`.

The interactive Swagger UI is available at `http://localhost:8083/swagger-ui/index.html` when using Docker, or `http://localhost:8082/swagger-ui/index.html` when running locally. This repository contains the API and Swagger UI, but no separate end-user frontend application.

## Authentication

Register using `POST /api/auth/register`:

```json
{
  "name": "Your Name",
  "email": "you@example.com",
  "password": "at-least-8-characters"
}
```

Or log in using `POST /api/auth/login`:

```json
{
  "email": "you@example.com",
  "password": "at-least-8-characters"
}
```

Both endpoints return a token and token type. Send the token with protected requests:

```http
Authorization: Bearer <token>
```

## API

All resource endpoints below require a bearer token. Request and response bodies use JSON.

| Resource | Method | Endpoint | Description |
| --- | --- | --- | --- |
| Authentication | `POST` | `/api/auth/register` | Register and receive a token |
| Authentication | `POST` | `/api/auth/login` | Log in and receive a token |
| Users | `POST` | `/api/users` | Create a user |
| Users | `GET` | `/api/users` | List users (paginated) |
| Users | `GET` | `/api/users/{id}` | Get a user |
| Users | `PUT` | `/api/users/{id}` | Update a user |
| Users | `DELETE` | `/api/users/{id}` | Delete a user |
| Categories | `POST` | `/api/categories` | Create a category |
| Categories | `GET` | `/api/categories` | List categories (paginated) |
| Categories | `GET` | `/api/categories/{id}` | Get a category |
| Categories | `PUT` | `/api/categories/{id}` | Update a category |
| Categories | `DELETE` | `/api/categories/{id}` | Delete a category |
| Expenses | `POST` | `/api/expenses` | Create an expense |
| Expenses | `GET` | `/api/expenses` | List expenses (paginated) |
| Expenses | `GET` | `/api/expenses/{id}` | Get an expense |
| Expenses | `PUT` | `/api/expenses/{id}` | Update an expense |
| Expenses | `DELETE` | `/api/expenses/{id}` | Delete an expense |

Paginated endpoints accept Spring Data parameters such as `page`, `size`, and `sort`, for example `GET /api/expenses?page=0&size=20&sort=expenseDate,desc`.

An expense request uses `title`, `amount`, `categoryId`, `expenseDate` (`YYYY-MM-DD`), and `userId`. A category request uses `name` and `userId`; a user request uses `name` and `email`.

Interactive API documentation is available at `http://localhost:8082/swagger-ui/index.html`; the OpenAPI spec is at `http://localhost:8082/v3/api-docs`.
