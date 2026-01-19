# Quickstart: Stabilization Audit

## Running Probes

### 1. Verification Journey
1. Navigate to `/contract/detail/1`.
2. Confirm that fields with `fill_type: AI` show a "Confirm" button.
3. Verify that clicking "Confirm" triggers `POST /api/v1/contracts/1/verify`.

### 2. Async Isolation Probe
1. Produce a Kafka message without `tenant-id` header.
2. Confirm the consumer logs a `SecurityException`.

### 3. Audit Diff Check
1. Change a contract amount.
2. Verify `audit_log` details column contains `{ "amount": { "old": "X", "new": "Y" } }`.
