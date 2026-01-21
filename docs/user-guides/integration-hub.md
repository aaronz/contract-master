# User Guide: Integration Hub

The **Integration Hub** is the central nerve center for connecting Contract Master with your external business ecosystem. It handles data synchronization with CRMs (e.g., Salesforce, HubSpot, DingTalk), ERPs, and internal finance systems using standardized protocols and secure authentication.

## 1. Overview

The Integration Hub supports flexible data flow patterns:
- **Inbound (Push)**: Automatic contract creation from external CRMs via WebHooks.
- **Outbound (Push)**: Persistent, reliable data delivery to downstream ERP/Finance systems via Kafka Outbox.
- **Outbound (Pull)**: Secure data retrieval for external systems via the Pull-Model API using API Keys.

## 2. Managing Downstream Systems

To integrate with an external system, you must first register it.
1. Navigate to **Integration Hub > Downstream Systems**.
2. Click **Add New System**.
3. **Authentication Types**:
   - `API_KEY` / `HMAC`: Standard static or signed keys.
   - `OAUTH2`: Modern OAuth2 Client Credentials flow. Provide `Client ID`, `Client Secret`, and `Token URL`.
4. **Connectivity**: Use the **Test Connection** button to verify the system is reachable.

## 3. Transformation Center (Field Mapping)

Data protocols are managed centrally in the **Field Mapping** view.
1. **Direction**: Choose `INBOUND` (from CRM) or `OUTBOUND` (to ERP).
2. **System**: Select the target system for this mapping.
3. **Advanced Transformations**: Use **Groovy Scripts** for complex logic.
   - Example: `return "Project-" + value`
4. **Data Masking**: For the Pull API, only fields explicitly mapped here will be included in the response.

## 4. Inbound Integration (WebHooks)

1. Configure your CRM to send POST requests to your **WebHook Endpoint**.
2. Ensure you have defined **INBOUND** field mappings to correctly parse incoming payloads.

## 5. Pull-Model API

External systems can pull data on-demand:
- **Endpoint**: `GET /api/v1/integration/contracts`
- **Header**: `X-API-KEY: <Your_Access_Key>`
- **Delta Sync**: Use `?updatedSince=YYYY-MM-DDTHH:mm:ss` to pull only changed records.

## 6. Monitoring & Recovery

- **Health Status**: Real-time "Healthy/Down" status via heartbeat monitoring.
- **Audit Logs**: View the full **Request Payload** for every attempt.
- **Manual Replay**: Click **Replay** on a failed log entry to re-enqueue the event immediately.

---
*For complex script requirements or ESB architecture questions, please contact the System Administrator.*
