# Tasks: Integration Hub

**Input**: Design documents from `/specs/005-integration-hub/`
**Prerequisites**: plan.md, spec.md, research.md, data-model.md

**Tests**: JUnit 5 for backend push logic; Playwright for Dashboard and Log Viewer interactions.

**Organization**: Tasks are structured to establish the outbound sync engine first, followed by the management UI components.

## Phase 1: Outbound Engine (Backend)

**Purpose**: Establish the core push and signing capabilities.

- [ ] T001 Implement `HmacSigner` utility in `backend/src/main/java/com/contract/master/integration/`
- [ ] T002 Implement `WebHookPushService` with asynchronous execution in `backend/src/main/java/com/contract/master/service/`
- [ ] T003 Create `IntegrationLog` repository and persistence logic in `backend/src/main/java/com/contract/master/domain/`
- [ ] T004 Implement retry logic with exponential backoff in `WebHookPushService`
- [ ] T005 Update `PublishController` to trigger the outbound push upon successful contract verification

## Phase 2: Integration Dashboard & API

**Purpose**: Provide endpoints and UI for monitoring sync status.

- [ ] T006 Implement `IntegrationLogController` with list and detail (payload) endpoints in `backend/src/main/java/com/contract/master/api/`
- [ ] T007 [P] Create `IntegrationDashboard.vue` with aggregate health metrics in `frontend/src/views/settings/`
- [ ] T008 [P] Implement `LogViewer.vue` component to display request/response JSON in `frontend/src/components/`
- [ ] T009 Implement manual "Retry" endpoint in `IntegrationLogController`

## Phase 3: UI Management Tools (Closure)

**Purpose**: Full management loop for integrations.

- [ ] T010 [P] Integrate manual retry and cancel controls into the Dashboard table in `frontend/src/views/settings/crm.vue`
- [ ] T011 [P] Implement "Downstream System" status overview cards (Health, Latency) in `frontend/src/views/settings/crm.vue`
- [ ] T012 Add "Live Stream" or auto-refresh capability to the integration log table

## Phase 4: Verification & E2E Testing

**Purpose**: Validate the outbound hub.

- [ ] T013 [US1] E2E Test: Verify "Publish" trigger sends signed payload to mock endpoint
- [ ] T014 [US3] E2E Test: Verify failed push appears in Dashboard and manual retry works
- [ ] T015 [US2] E2E Test: Verify HMAC signature validation on outbound messages

## Phase 5: Polish & Documentation

- [ ] T016 [VII] Finalize `quickstart.md` with outbound sync testing instructions
- [ ] T017 [VIII] Perform UX review of the retry/failure feedback loop
