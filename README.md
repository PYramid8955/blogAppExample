# Blog Application

A simple blog backend built with Spring Boot, PostgreSQL, Liquibase, and Spring Security. Provides CRUD operations for users, posts, and comments with Basic Authentication.

## Technologies

- Java 21
- Spring Boot
- Spring Data JPA
- Spring Security (Basic Auth)
- PostgreSQL
- Liquibase
- Maven
- Docker / Docker Compose

## Prerequisites

- Docker and Docker Compose (for containerized run)
- Java 21 and Maven (for local development)
- Postman (for testing)

## Configuration

The application uses the following default properties (can be overridden via environment variables):

- Database: `blogdb`, user `postgres`, password `postgres` (when using Docker Compose)
- Server port: `8080`

## Running with Docker Compose (recommended)

1. Clone the repository:
   `gh repo clone PYramid8955/blogAppExample`
   `cd blog`

2. Build and start containers:
   `docker compose build`
   `docker compose up -d`
   
4. The application will be available at `http://localhost:8080`. You could use Postman or similar programs to test the endpoints.
