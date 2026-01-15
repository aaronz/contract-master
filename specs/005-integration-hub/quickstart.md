# Quickstart: Integration Hub

## Configuration
1. Go to **Settings -> Downstream Systems**.
2. Add a new target URL (e.g., `https://webhook.site/your-id`).
3. Set a **Secret Key** for HMAC signing.

## Testing Outbound Sync
1. Navigate to any contract in `VERIFIED` status.
2. Click **Publish to Downstream**.
3. Go to **Settings -> Integration Hub** to monitor the delivery status.
4. Verify the JSON payload and `X-Hub-Signature` at the target URL.

## Manual Retry
If a sync fails (e.g., target system was down):
1. Locate the failed attempt in the Integration Hub table.
2. Click the **Retry** icon.
3. Observe the status update to `SUCCESS` once the target is back online.
