# Quickstart: Contract Master Core System

## Development Environment Setup

### 1. Prerequisites
- **JDK 17** (Temurin recommended)
- **Maven 3.8+**
- **Docker & Docker Compose** (for PostgreSQL, Redis, Kafka, MinIO)
- **Node.js 18+ & npm** (for frontend)

### 2. Infrastructure Setup
```bash
docker-compose up -d
```
*Note: Ensure `X-Tenant-ID` header is used in all API requests for testing.*

### 3. Backend Setup
1. Configure environment variables in `backend/.env`:
   - `DB_URL=jdbc:postgresql://localhost:5432/contract_master`
   - `AZURE_AI_KEY=your_key`
   - `KAFKA_BOOTSTRAP=localhost:9092`
2. Run migration and start:
   ```bash
   mvn spring-boot:run
   ```

### 4. Frontend Setup
1. Install dependencies:
   ```bash
   cd frontend && npm install
   ```
2. Start dev server:
   ```bash
   npm run dev
   ```

## Core Workflows

### 1. Synchronize from CRM
- Configure WebHook URL in Salesforce/HubSpot to: `http://{host}/api/v1/webhooks/{source}`
- Send a test payload and verify record in `contract_base`.

### 2. AI Extraction
- Upload a contract PDF via the UI.
- Click "Extract Elements".
- Verify data populated in "Extension Fields" section.

### 3. Rule Validation
- Update contract data to trigger Drools validation.
- Check "Compliance Status" badge for warnings/errors.
