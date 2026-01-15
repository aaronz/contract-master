# Quickstart: Contract Master Core Enhancements

## Prerequisites
- Java 17
- Node.js 18+
- PostgreSQL running

## Backend Setup
1. Navigate to `backend/`
2. Run `./mvnw spring-boot:run`
3. Verify Metadata API: `curl http://localhost:8080/api/metadata/fields`

## Frontend Setup
1. Navigate to `frontend/`
2. Run `npm install`
3. Run `npm run dev`
4. Access `http://localhost:5173/settings/fields` to verify field list.

## Testing
1. **Unit Tests**: `mvn test`
2. **E2E**: `./run_tests.sh` (ensure frontend is running)
