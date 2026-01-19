# Contract Master

Contract Master is a sophisticated contract management middleware designed to bridge the gap between SaaS CRM systems (such as Salesforce, HubSpot, and DingTalk) and enterprise internal business systems. It provides a unified data model for contracts, enhanced by an intelligent rule engine and AI-driven insights for compliance risk detection and data integrity.

## Project Overview

The system serves as a central hub for contract data, ensuring standardization and accuracy through:
- **Data Integration**: Real-time synchronization from multiple CRM platforms.
- **Rule Engine**: A flexible, visual logic engine for compliance and risk validation.
- **AI Extraction**: Automated parsing of PDF/Word documents to supplement contract metadata.
- **Multi-tenancy**: Strict logical isolation of data and configurations across different organizations.

## Tech Stack

### Backend
- **Core**: Java 17, Spring Boot 3.2.x
- **Data**: Spring Data JPA, Hibernate 6.x
- **Rule Engine**: Drools 9.x
- **AI**: Spring AI (OpenAI/Azure integration)
- **Messaging**: Spring Kafka
- **Mapping**: MapStruct

### Frontend
- **Framework**: Vue 3 (Composition API)
- **UI Components**: Element Plus
- **Build Tool**: Vite
- **State Management**: Pinia
- **Utility**: VueUse, ECharts, PDF.js

### Infrastructure
- **Database**: PostgreSQL (with JSONB support)
- **Cache**: Redis
- **Storage**: MinIO (Object Storage)

### Testing
- **E2E**: Playwright 1.41.2
- **Unit/Integration**: JUnit 5, AssertJ

## Project Structure

```text
contract-master/
├── backend/          # Spring Boot application (DDD-based architecture)
├── frontend/         # Vue 3 + Vite frontend application
├── tests/            # Playwright E2E test suite
├── scripts/          # Automation scripts (Dev, Test, E2E)
└── docs/             # Project documentation and specifications
```

## Key Features

- **Contract Lifecycle Management**: Comprehensive CRUD operations, advanced search, and lifecycle status tracking (Draft -> Pending -> Active -> Expired).
- **Visual Rule Engine**: Supports LOGIC, GROOVY, and REGEX rules to detect compliance risks, data gaps, and logical conflicts.
- **Compliance Cockpit**: A centralized dashboard for risk detection, real-time alerts, and issue tracking to ensure contract health.
- **Real-time Evaluation**: Asynchronous re-evaluation of contracts triggered by data updates or rule changes via Kafka.
- **AI-Powered Extraction**: Automatically extracts key elements from uploaded contract documents using NLP and OCR.
- **Multi-tenant Isolation**: Securely manages multiple organizations with tenant-specific configurations and data boundaries.

## Getting Started

### Prerequisites
- Java 17
- Node.js (v18+)
- Docker (for PostgreSQL, Redis, and MinIO)

### One-Click Development
Run the development environment script:
- **Unix/macOS**: `./scripts/dev.sh`
- **Windows**: `scripts\dev.bat`

### Manual Execution

#### Backend
```bash
cd backend
./mvnw spring-boot:run
```

#### Frontend
```bash
cd frontend
npm install
npm run dev
```

### Running Tests
- **Unix/macOS**: `./scripts/run_tests.sh`
- **Windows**: `scripts\run_tests.bat`

## Development Guidelines

- **Domain-Driven Design (DDD)**: The project adheres to DDD principles. Ensure business logic is encapsulated within domain services and entities.
- **E2E Testing**: All critical user paths must be covered by Playwright E2E tests located in the `tests/` directory.
- **Documentation**: All specifications and architecture documents are located in the `docs/` directory.
- **Code Style**: Follow standard Java and Vue 3 style conventions.

---
*Built with precision by the Contract Master Team.*
