# Software Architecture Documentation - Contract Master

This directory contains the architecture documentation for the Contract Master system, following the C4 model.

## Overview

Contract Master is a sophisticated contract management middleware designed to bridge the gap between SaaS CRM systems and enterprise internal business systems. The following diagrams describe its architecture at different levels of abstraction.

## C4 Model Diagrams

1.  [**Level 1: System Context Diagram**](c4-context.md)
    Provides a high-level view of the system, its users, and its external dependencies (CRMs, AI Providers, Enterprise Systems).

2.  [**Level 2: Container Diagram**](c4-containers.md)
    Describes the high-level technical building blocks, including the Frontend SPA, Backend API, Databases, and Message Broker.

3.  [**Level 3: Component Diagrams**](c4-components.md)
    Details the internal structure of key modules within the system:
    *   [Integration Hub](c4-components-integration.md) - Manages data synchronization and external connections.
    *   [Rule Engine & Compliance](c4-components-rule-engine.md) - Handles contract validation and risk detection.

## Tech Stack Summary

*   **Frontend**: Vue 3, Vite, Element Plus, Pinia.
*   **Backend**: Java 17, Spring Boot 3.2.x, Spring Data JPA, Spring Security.
*   **Infrastructure**: PostgreSQL (Main DB), Redis (Cache), Kafka (Messaging), MinIO/S3 (Storage).
*   **Intelligence**: Groovy/Regex Rule Engine, Drools, LLM Integration (OpenAI API).

## Principles

*   **DDD (Domain-Driven Design)**: The backend is structured into clear domain contexts (Contract, Identity, Integration, Rule, etc.).
*   **Multi-Tenancy**: Transparent tenant isolation across the entire system.
*   **Metadata-Driven**: Highly configurable contract fields and validation rules.
