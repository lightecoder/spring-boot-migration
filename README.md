
# Spring Boot Kitchensink Migration

This project is a migration of the legacy `kitchensink` JBoss application to Spring Boot with MongoDB.

## Prerequisites

- Java 21
- Maven
- Docker and Docker Compose

## Getting Started

1. Clone the repository:
   ```bash
   git clone https://github.com/lightecoder/spring-boot-migration.git
   ```

2. Build the project:
   ```bash
   mvn clean package
   ```

3. Start the application using Docker Compose:
   ```bash
   docker-compose up --build
   ```

This will:

Build the Spring Boot application.
Start a MongoDB container for the database.
Start the Spring Boot application that connects to MongoDB.


4. Access the application:
    - Spring Boot app: `http://localhost:8080`
    - MongoDB: Connected internally via Docker (`mongodb://mongodb:27017`)

## Endpoints

- **POST /api/members**: Add a new member.
- **GET /api/members**: Get all members.
- **GET /api/members/{id}**: Get a member by ID.
- **DELETE /api/members/{id}**: Delete a member by ID.

## Connecting to MongoDB

1. To access the MongoDB CLI:
   ```bash
   docker exec -it mongodb mongo
   ```

2. To query members collection:
   ```bash
   use kitchensinkdb
   db.members.find()
   ```
   
## Access the Registration UI

The application includes a simple Thymeleaf-based form for member registration.

UI URL: http://localhost:8080/members/register

### Registration Endpoints

 - GET /members/register: Display the registration form.
 - POST /members/register: Submit the member registration form.

## Application Details

### The following services and components are part of the migration:

 - Member Controller: Provides the necessary endpoints for handling member data and the registration form.
 - Member Repository: Uses Spring Data MongoDB to manage CRUD operations for member entities.
 - Member Registration Service: Handles business logic for registering members.
 - Member Event Listener: Demonstrates event handling using Spring's @EventListener.

## Error Handling

The application includes a global exception handler (GlobalExceptionHandler) to capture validation errors and entity-not-found scenarios.

## Notes

This migration involves changing from a relational database to MongoDB as the data store,
and uses Spring Data MongoDB for persistence. The legacy JBoss EAP application functionality has been preserved,
but the architecture has been updated for modern development practices using Spring Boot.