# Quickstart: Working with the ESB Hub

## 1. Defining a Transformation Script
In the Field Mapping UI, you can now enter Groovy logic:
```groovy
// Example: Convert amount to major units and prefix currency
return "USD " + (value / 100)
```

## 2. Setting up OAuth2
When registering a system, select `OAUTH2` and provide:
- `Client ID`
- `Client Secret`
- `Token URL` (e.g., https://login.salesforce.com/services/oauth2/token)

## 3. Monitoring Failures
Visit the **Integration Dashboard**.
- If a row is **RED**, click "View Payload" to see exactly what failed.
- Fix the mapping or target system.
- Click **Replay** to resend the exact payload.

## 4. Automatic Retries
The system uses Kafka retries:
- Attempt 1: Immediate
- Attempt 2: +10 seconds
- Attempt 3: +1 minute
- Attempt 4: +10 minutes
- Final: Moves to **DEAD_LETTER** status in logs.
