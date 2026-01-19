# Quickstart: Backend DDD Refactor

This guide describes how to work with the new DDD-based backend architecture.

## Architecture Overview

The system follows a Hexagonal / Ports and Adapters structure:

1.  **Interfaces Layer**: `interfaces.rest`
    - Handles HTTP requests/responses.
    - Uses DTOs for data transfer.
2.  **Application Layer**: `application.service`
    - Orchestrates domain logic.
    - Transaction boundaries are defined here.
3.  **Domain Layer**: `domain.model` & `domain.service`
    - Contains the core business logic.
    - **Entities** and **Aggregate Roots** handle their own consistency.
    - **Value Objects** are immutable and handle business calculations.
    - **Repository Interfaces** define persistence needs.
4.  **Infrastructure Layer**: `infrastructure.persistence`
    - Implements repository interfaces using JPA/Hibernate.
    - Maps domain models to database entities.

## Key Development Workflows

### 1. Adding a new Business Rule
- Add a method to the relevant **Aggregate Root** (e.g., `Contract.java`).
- Write a unit test for that aggregate root in `src/test/java/.../domain/model/`.

### 2. Adding a new Persistence Field
- Update the SQL table in `table.md`.
- Update the **Domain Model** (Entity/Value Object).
- Update the **Infrastructure Entity** and its **Mapper** (e.g., MapStruct).

### 3. Running Tests
- **Unit Tests**: `mvn test` (Should cover 90% of business logic).
- **Integration Tests**: `mvn verify` (Verifies persistence and infrastructure).
