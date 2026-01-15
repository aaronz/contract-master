# Quickstart: UI Stability and Feature Completion

## Prerequisites
- Java 17
- Node.js 18+
- PostgreSQL running

## Backend Setup
1. Navigate to `backend/`
2. Run `./mvnw spring-boot:run`
3. Verify API is up at `http://localhost:8080/api/health`

## Frontend Setup
1. Navigate to `frontend/`
2. Run `npm install`
3. Run `npm run dev`
4. Access UI at `http://localhost:5173`

## Running Tests
1. Backend Unit Tests: `mvn test`
2. E2E Tests: `./run_tests.sh` (ensure frontend is running)
