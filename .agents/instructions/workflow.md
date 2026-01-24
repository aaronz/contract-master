# Development Workflow

## 1. Feature Lifecycle
- **Specification**: Create `spec.md` including a "Constitution Check".
- **Planning**: Create `plan.md` with implementation steps.
- **Execution**: Break down into `tasks.md`.
- **Manifest Updates**: Refresh `features.md`, `bugs.md`, `api.md`, and `table.md`.

## 2. Code Review Requirements
- Adherence to Core Principles (Isolation, Audit, Propagation).
- Design documents (spec/plan/tasks) are in sync with implementation.
- Global manifests are updated.

## 3. Commands
- **Backend**: Standard Maven/Spring Boot commands (Java 17).
- **Frontend**: `npm run dev`, `npm run build` (Vue 3/Vite).
- **Tests**: Playwright E2E suite in `tests/`.
