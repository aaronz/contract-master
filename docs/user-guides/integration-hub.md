# User Guide: Integration Hub

The **Integration Hub** is the central nerve center for connecting Contract Master with your external business ecosystem. It handles data synchronization with CRMs (e.g., Salesforce, HubSpot, DingTalk), ERPs, and internal finance systems using standardized protocols and secure authentication.

## 1. Overview

The Integration Hub supports two-way data flow:
- **Inbound**: Automatic contract creation and updates from external CRMs via WebHooks.
- **Outbound**: Pushing finalized contract data to downstream ERP or Finance systems.

## 2. Managing Downstream Systems

To send contract data to an external system, you must first register it as a "Downstream System".

### Steps to Register a System:
1. Navigate to **Settings > Integration Hub > Downstream Systems**.
2. Click **Add New System**.
3. Provide the following details:
   - **System Name**: A recognizable name (e.g., "SAP Finance").
   - **Endpoint URL**: The target API URL where data will be sent.
   - **Authentication Type**: Choose from `NONE`, `HMAC`, or `API_KEY`.
   - **Status**: Set to `Enabled` to start the sync.
4. Click **Save**. The system will generate a unique `Access Key` for HMAC signing if required.

## 3. Field Mapping

External systems often use different field names than Contract Master. Use **Field Mapping** to translate data between systems.

### Configuration:
1. Go to **Settings > Integration Hub > Field Mappings**.
2. Define mappings for each external system:
   - **Internal Field**: Select a field from Contract Master (Standard or Extended).
   - **External Field**: Enter the corresponding field name in the target system.
   - **Transformation**: (Optional) Choose a rule like `TO_UPPERCASE` or `DATE_FORMAT`.
3. **Enabled**: Ensure the mapping is active.

## 4. Inbound Integration (WebHooks)

Receive data from your CRM automatically by configuring WebHooks.

### Setup:
1. Go to **Settings > Integration Hub > WebHook Configs**.
2. Note your **WebHook Endpoint URL** (e.g., `/api/webhook/salesforce`).
3. Configure your CRM to send POST requests to this URL whenever a contract/opportunity is updated.
4. Select the **Events** you wish to subscribe to (e.g., `CONTRACT_CREATED`, `CONTRACT_SIGNED`).

## 5. Monitoring and Logs

The **Integration Dashboard** provides real-time visibility into the health of your connections.

- **Sync Success Rate**: Percentage of successful outbound pushes.
- **Activity Feed**: A list of the last 10 integration events.
- **Integration Logs**: Detailed logs for every sync attempt, including error messages and duration. If a sync fails, check the logs for the specific HTTP error code and payload.

## 6. Security Best Practices

- **Rotate Keys**: Periodically regenerate API keys and HMAC secrets.
- **IP Whitelisting**: If possible, restrict inbound WebHook traffic to the IP ranges of your CRM provider.
- **Tenant Isolation**: Remember that all integration settings (mappings, systems, logs) are strictly isolated per tenant. Ensure you are in the correct tenant context before making changes.

---
*For technical support or complex transformation requirements, please contact the System Administrator.*
