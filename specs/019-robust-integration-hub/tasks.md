# Tasks: Robust Integration Hub (ESB Foundation)

**Input**: Design documents from `/specs/019-robust-integration-hub/`
**Prerequisites**: plan.md, spec.md, research.md, data-model.md

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2, US3)
- Include exact file paths in descriptions

---

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Project initialization and base dependencies

- [X] T001 Verify branch `019-robust-integration-hub` and project structure
- [X] T002 [P] Add Groovy and Caffeine dependencies to `backend/pom.xml`
- [X] T003 [P] Implement `IntegrationEvent` DTO for Kafka messaging in `backend/src/main/java/com/contract/master/integration/domain/model/IntegrationEvent.java`
- [X] T004 [P] Update `FieldMapping` entity with `transformationScript` in `backend/src/main/java/com/contract/master/integration/domain/model/FieldMapping.java`
- [X] T005 [P] Update `IntegrationLog` entity with `requestPayload` and `retryCount` in `backend/src/main/java/com/contract/master/integration/domain/model/IntegrationLog.java`
- [X] T006 [P] Update `DownstreamSystem` entity with health and OAuth2 fields in `backend/src/main/java/com/contract/master/integration/domain/model/DownstreamSystem.java`
- [X] T007 Implement `ScriptSandbox` using `GroovyShell` with restricted imports in `backend/src/main/java/com/contract/master/integration/domain/service/ScriptSandbox.java`
- [X] T008 [P] [US1] Create `IntegrationKafkaProducer` to publish events in `backend/src/main/java/com/contract/master/integration/infrastructure/IntegrationKafkaProducer.java`
- [X] T009 [US1] Refactor `IntegrationPushService` to publish Kafka events instead of direct `@Async` calls in `backend/src/main/java/com/contract/master/integration/application/IntegrationPushService.java`
- [X] T010 [US1] Implement `IntegrationKafkaConsumer` to handle pushes and retries in `backend/src/main/java/com/contract/master/integration/infrastructure/IntegrationKafkaConsumer.java`
- [X] T011 [US1] Configure Kafka `DefaultErrorHandler` with `ExponentialBackOff` in `backend/src/main/java/com/contract/master/config/KafkaConfig.java`
- [X] T012 [US2] Update `IntegrationPushService.transformData` to utilize `ScriptSandbox` for field mappings in `backend/src/main/java/com/contract/master/integration/application/IntegrationPushService.java`
- [X] T013 [P] [US2] Add unit tests for `ScriptSandbox` covering security and timeouts in `backend/src/test/java/com/contract/master/integration/domain/service/ScriptSandboxTest.java`
- [X] T014 [US3] Implement Replay API endpoint in `backend/src/main/java/com/contract/master/integration/interfaces/rest/IntegrationHubController.java`
- [X] T015 [US3] Implement `IntegrationReplayService` to re-enqueue original payloads in `backend/src/main/java/com/contract/master/integration/application/IntegrationReplayService.java`
- [X] T016 [US4] Implement `OAuth2TokenManager` with Caffeine caching in `backend/src/main/java/com/contract/master/integration/application/OAuth2TokenManager.java`
- [X] T017 [US4] Update `IntegrationKafkaConsumer` to use `OAuth2TokenManager` for outbound requests in `backend/src/main/java/com/contract/master/integration/infrastructure/IntegrationKafkaConsumer.java`
- [X] T018 [US5] Implement `@Scheduled` `IntegrationHealthCheckTask` in `backend/src/main/java/com/contract/master/integration/application/IntegrationHealthCheckTask.java`
- [X] T019 [P] [US5] Implement manual "Test Connection" API in `backend/src/main/java/com/contract/master/integration/interfaces/rest/DownstreamSystemController.java`


---

## Phase 8: Polish & Cross-Cutting Concerns

**Purpose**: Final audit and documentation sync

- [ ] T020 Update `api.md` with new Replay and Health APIs in `docs/api.md`
- [ ] T021 Update `table.md` with new entity fields in `docs/table.md`
- [ ] T022 [VII] Synchronize `quickstart.md` with final implementation in `specs/019-robust-integration-hub/quickstart.md`

---

## Dependencies & Execution Order

### Phase Dependencies

1. **Foundational (Phase 2)**: Depends on Phase 1. BLOCKS all user stories.
2. **Reliability (US1)**: Priority P1. Must complete before Replay (US3).
3. **Transformation (US2)**: Priority P2. Can run in parallel with US1.
4. **Observability (US3)**: Depends on US1 (to have events to replay).
5. **Security (US4)**: Priority P3.
6. **Health (US5)**: Priority P3.

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Complete Setup + Foundational tasks (T001-T007).
2. Implement Kafka Producer/Consumer (T008-T011).
3. Verify reliability with application restart during push.

### Parallel Opportunities

- T003, T004, T005, T006 can be handled concurrently.
- US1 (Reliability) and US2 (Transformation) can be developed by different teams once Foundation is ready.
