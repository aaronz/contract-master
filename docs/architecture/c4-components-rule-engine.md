# Component Diagram - Rule Engine & Compliance

The Component diagram for the Rule Engine shows how contract compliance is evaluated using dynamic rules.

```mermaid
C4Component
  title Component Diagram - Rule Engine & Compliance

  Container(spa, "Single-Page App", "Vue 3", "User Interface")
  ContainerDb(db, "Main Database", "PostgreSQL", "Stores rules and evaluation results")
  ContainerQueue(kafka, "Message Broker", "Kafka", "Triggers evaluation")

  Container_Boundary(apiApp, "API Application") {
    Component(evaluationController, "Evaluation Controller", "Spring REST Controller", "API for triggering and monitoring contract evaluations.")
    Component(ruleController, "Rule Controller", "Spring REST Controller", "API for managing evaluation rules.")
    Component(evaluationService, "Evaluation Application Service", "Spring Service", "Orchestrates the evaluation process and manages evaluation jobs.")
    Component(ruleEngineService, "Rule Engine Domain Service", "Spring Service", "Domain-level logic for selecting and executing rules.")
    Component(droolsEngine, "Drools Engine", "Drools", "Executes complex business rules defined in .drl files.")
    Component(scriptExecutor, "Script Executor", "Groovy / Regex", "Executes dynamic rules defined as Groovy scripts or Regular Expressions.")
    Component(problemGenerator, "Problem Generator", "Spring Service", "Creates 'Problem' entities in the Problem Center based on evaluation failures.")
  }

  Rel(spa, evaluationController, "Triggers evaluation", "HTTPS")
  Rel(spa, ruleController, "Manages rules", "HTTPS")
  
  Rel(evaluationController, evaluationService, "Calls")
  Rel(ruleController, ruleEngineService, "Manages rules through")
  
  Rel(evaluationService, ruleEngineService, "Requests evaluation")
  Rel(ruleEngineService, droolsEngine, "Executes DRL rules")
  Rel(ruleEngineService, scriptExecutor, "Executes dynamic rules")
  Rel(ruleEngineService, problemGenerator, "Notifies of violations")
  
  Rel(evaluationService, db, "Saves evaluation state", "JPA")
  Rel(ruleEngineService, db, "Reads rules", "JPA")
  Rel(problemGenerator, db, "Creates problem records", "JPA")
  
  Rel(kafka, evaluationService, "Triggers async evaluation")
```

## Components

| Component | Technology | Description |
|-----------|------------|-------------|
| **Evaluation Service** | Spring Service | Manages the lifecycle of evaluation jobs and async execution. |
| **Rule Engine Service** | Spring Service | The core logic for rule execution, supporting multiple rule types. |
| **Drools Engine** | Drools | High-performance production rule engine for complex logic. |
| **Script Executor** | Groovy/Regex | Lightweight executors for user-defined dynamic logic. |
| **Problem Generator** | Spring Service | Integration point between evaluation results and the Problem Center. |
