# UI & User Journey Gap Analysis Report

## 1. Executive Summary
This third gap analysis focuses on the usability and administrative completeness of the Contract Master UI. While the core "middleware" functions (sync, AI extraction) are implemented in the backend, the UI currently lacks the "control plane" necessary for tenant self-service. The user journey from sync to publish is fragmented due to missing action triggers and feedback mechanisms.

## 2. UI Management Gaps (Admin Console)

### 2.1 Missing Downstream Management (Critical)
- **Problem**: There is no UI to configure where data is published after verification.
- **Remediation**: Implement a `DownstreamSystem` management view in `/settings`.

### 2.2 Incomplete Rule Editor (High)
- **Problem**: Drools rules can only be managed via raw DRL strings in the database.
- **Remediation**: Create a structured editor for rule conditions and actions.

### 2.3 Field Type Parity (Medium)
- **Problem**: UI currently only handles `TEXT` and `NUMBER` types effectively.
- **Remediation**: Ensure `DATE`, `ENUM`, and `ATTACHMENT` types are supported in the dynamic renderer.

## 3. User Journey Gaps (Lifecycle)

### 3.1 Verification Bottleneck (High)
- **Problem**: The "Confirm" button for AI suggestions is missing. Users cannot progress contracts to `VERIFIED` state.
- **Remediation**: Add a "Confirm Verification" trigger to `ContractDetail.vue`.

### 3.2 Publication Dead-End (High)
- **Problem**: No "Publish" action exists in the UI.
- **Remediation**: Implement the `PUBLISH` state transition button and status feedback.

### 3.3 Audit Readability (Medium)
- **Problem**: Audit logs show "Action: UPDATE" but not "Field: Amount changed from 500 to 600".
- **Remediation**: Parse the `details` JSON in `audit.vue` to show field-level diffs.
