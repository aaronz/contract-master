# Data Model: UI Test Entities

These entities represent the test data required to drive the UI scenarios. They are **not** new database tables, but strictly test data factories.

## 1. Test User Roles

| Role | Permissions | Usage |
|------|-------------|-------|
| `Tenant Admin` | All permissions | Configuration tests |
| `Compliance Officer` | Create/Edit Contracts | Contract Lifecycle tests |
| `Risk Manager` | View Problems, Resolve | Risk Discovery tests |

## 2. Test Contract States

| State | Attributes | Usage |
|-------|------------|-------|
| `Draft Contract` | Status: DRAFT, Amount: 1000 | Editing verification |
| `Active Contract` | Status: ACTIVE, Signed | Read-only view verification |
| `Risky Contract` | Amount: 1M+, Region: High Risk | Rule trigger verification |

## 3. Test Rules

| Rule Type | Logic | Trigger |
|-----------|-------|---------|
| `High Value Check` | Amount > 500k | Contract Creation/Update |
| `Restricted Region` | Region IN [Sanctioned List] | Contract Creation/Update |
