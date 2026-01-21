# Implementation Plan: AI-Based Rule Type

This document outlines the plan to implement a new "AI" based rule type in the Contract Master system. This feature will allow users to define compliance rules using natural language prompts, which will be evaluated by an LLM.

## 1. Overview
Currently, the system supports GROOVY, REGEX, and JSON_LOGIC/LOGIC (SpEL) rules. Adding an AI rule type leverages existing LLM integration infrastructure (used for data extraction) to perform complex, semantic compliance checks on contract text.

## 2. Backend Changes

### 2.1. Domain Model Updates
- **`RuleLogicType` Enum**: Add `AI` to the list of supported logic types.
- **`Rule` Entity**:
    - Ensure `logicContent` can store the natural language prompt.
    - (Optional) Add `executionMode` (AUTOMATIC, MANUAL_ONLY) to manage costs.
- **`Problem` Entity**:
    - Add `aiReasoning` (TEXT) to store the detailed explanation from the LLM.
    - Add `llmModelName` (VARCHAR) for traceability.

### 2.2. Rule Execution Infrastructure
- **`AIRuleExecutor`**:
    - Implement `RuleExecutor` interface.
    - Logic:
        1. Retrieve `AISetting` for the tenant.
        2. Extract contract text (from attachments or metadata).
        3. Build a prompt combining the rule's natural language requirement + contract context.
        4. Call the configured LLM.
        5. Parse the JSON response (Violation status, Reasoning, Location).
- **`RuleExecutionResult`**:
    - Extend to include a `metadata` map to carry AI reasoning and other details back to the caller.

### 2.3. Async Evaluation (`EvaluationConsumer`)
- The `getExecutor` method will automatically pick up the new `AIRuleExecutor` bean.
- Update `createProblem` to extract AI-specific metadata from `RuleExecutionResult` and populate the `Problem` entity's new fields.

### 2.4. LLM Integration
- Reuse `AIExtractionService` and `AIService` patterns for calling LLM providers (SiliconFlow, DeepSeek, OpenAI, etc.).
- Implement "Smart Truncation" or summarization to stay within token limits when sending full contract text.

## 3. Frontend Changes

### 3.1. Rule Management UI
- **`RuleList.vue` / `RuleEditorForm.vue`**:
    - Add "AI Prompt" as a rule type option.
    - When selected, show a large textarea for the "AI Prompt Template" instead of the condition builder.
    - Add tooltips/placeholders to guide users in writing effective prompts.

### 3.2. Compliance Dashboard
- Update the problem details display to show "AI Reasoning" and "AI Model" when available.
- Ensure highlighted text returned by AI is visually represented.

## 4. Implementation Steps

### Phase 1: Foundation (Backend)
1. Update `RuleLogicType` enum.
2. Extend `RuleExecutionResult` and `Problem` entities/DTOs.
3. Create `AIRuleExecutor` skeleton.

### Phase 2: AI Logic (Backend)
1. Implement prompt building logic in `AIRuleExecutor`.
2. Implement LLM calling and response parsing.
3. Add token management/truncation logic.

### Phase 3: UI Integration (Frontend)
1. Update Rule Editor to support the `AI` logic type.
2. Update Problem Detail views to display AI reasoning.

### Phase 4: Testing & Optimization
1. Test with various LLM providers.
2. Optimize prompts for accuracy and cost.
3. Implement execution frequency controls (e.g., skip AI rules for certain triggers).

## 5. Security & Performance Considerations
- **Cost Management**: AI rules can be expensive; consider adding per-tenant quotas.
- **Latency**: LLM calls are slow; ensure the Kafka-based async evaluation handles timeouts gracefully.
- **Data Privacy**: Ensure sensitive contract data is handled according to tenant settings when sent to external LLM providers.
