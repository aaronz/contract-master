# Contract Master (合同大师)

[English](./README.md) | [简体中文](./README_zh.md)

Contract Master is a sophisticated contract management middleware designed to bridge the gap between SaaS CRM systems (such as Salesforce, HubSpot, and DingTalk) and enterprise internal business systems. It provides a unified data model for contracts, enhanced by an intelligent rule engine and AI-driven insights for compliance risk detection and data integrity.

## Project Overview

The system serves as a central hub for contract data, ensuring standardization and accuracy through:
- **Data Integration**: Real-time synchronization from multiple CRM platforms and outbound push services.
- **Rule Engine**: A flexible, visual logic engine supporting LOGIC, GROOVY, and REGEX for compliance and risk validation.
- **AI Intelligence**: Automated parsing of PDF/Docx documents using LLMs (SiliconFlow, OpenAI, DeepSeek) to supplement contract metadata.
- **Identity & Access (RBAC)**: Full role-based access control with multi-tenant isolation and granular data scoping (Global/Department/Self).

## Tech Stack

### Backend
- **Core**: Java 17, Spring Boot 3.2.x
- **Data**: Spring Data JPA (Hibernate 6.x), H2/PostgreSQL
- **Rule Engine**: Custom Script Engine (Groovy/Regex)
- **AI**: Integration with LLM providers via OpenAI-compatible APIs
- **Document**: Apache PDFBox for content extraction
- **Messaging**: Spring Kafka for asynchronous evaluation

### Frontend
- **Framework**: Vue 3 (Composition API)
- **UI Components**: Element Plus
- **Build Tool**: Vite
- **Internationalization**: Vue I18n (Support for EN/ZH)
- **State Management**: Pinia

## Project Structure

```text
contract-master/
├── backend/          # Spring Boot application (DDD-based architecture)
├── frontend/         # Vue 3 + Vite frontend application
├── tests/            # Playwright E2E test suite
├── scripts/          # Automation scripts (Dev, Env Check, E2E)
└── docs/             # Project documentation and specifications
```

## Key Features

- **AI-Powered Extraction**: Automatically extracts 5+ key fields from uploaded PDF/Docx documents with visual highlighting for verification.
- **Unified Compliance Cockpit**: Centralized risk detection dashboard with real-time alerts and issue lifecycle tracking.
- **Dynamic Permission Matrix**: Configure module access and data visibility ranges per role with immediate enforcement.
- **Integration Hub**: Define outbound webhooks and field mapping rules to distribute verified data to downstream ERP/CRM systems.
- **Audit Trails**: Complete history of system changes and user activities for compliance tracking.

## Getting Started

### Prerequisites
- **Java 17** or higher
- **Node.js** (v18+)
- **Docker** (for infrastructure services)

### One-Click Development
Run the development environment script (includes automatic dependency checks):
- **Unix/macOS**: `./scripts/dev.sh`
- **Windows**: `scripts\dev.bat`

## Release Notes
For detailed version history and feature updates, please refer to the **Release Notes** section within the system UI (accessible via the Clock icon in the header).

---
*Built with precision for modern enterprise contract compliance.*
