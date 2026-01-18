# Research: Simplify Backend Logs

## 1. Confirm Logging Framework

**Task**: Research and confirm the logging framework used in the backend.

**Decision**: The logging framework is confirmed to be **Logback**, used with SLF4J.

**Rationale**:
- The project's `defects_report.md` explicitly mentions migrating from `System.out.println` to "SLF4J/Logback".
- The implementation plan for this feature identifies `logback-spring.xml` as the likely configuration file, which is the standard for Spring Boot applications using Logback.

**Alternatives considered**:
- **Log4j2**: Another popular logging framework. However, the evidence points to Logback being the established standard for this project.
