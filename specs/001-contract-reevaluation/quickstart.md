# Quickstart: Contract Re-evaluation API

This guide explains how to use the new API endpoint to trigger a contract re-evaluation.

## Endpoint

`POST /api/evaluations`

## Request Body

The request body must be a JSON object with the following fields:

-   `contractId` (string, required): The ID of the contract to re-evaluate.
-   `ruleIds` (array of strings, required): A list of rule IDs to use for the evaluation.

**Example**:

```json
{
  "contractId": "c4d2b8e0-1c8e-4b2a-8d3a-0b9e8c7f6a5b",
  "ruleIds": [
    "r1b2c3d4-e5f6-7890-1234-567890abcdef",
    "r9a8b7c6-d5e4-f3g2-h1i0-j9k8l7m6n5o4"
  ]
}
```

## Responses

-   **202 Accepted**: The evaluation job was successfully created and is pending execution. The response body will contain the `jobId`.

    ```json
    {
      "jobId": "j1a2b3c4-d5e6-f7g8-h9i0-j1k2l3m4n5o6"
    }
    ```

-   **400 Bad Request**: The request was invalid (e.g., missing `contractId` or `ruleIds`).
-   **404 Not Found**: The specified `contractId` or one of the `ruleIds` could not be found.
-   **409 Conflict**: An evaluation is already in progress for the specified `contractId`.

## Authentication

All requests must be authenticated and include the `X-Tenant-ID` header.
