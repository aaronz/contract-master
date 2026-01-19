# Quickstart: Problem Center and Rule Engine

## 1. Defining a New Rule
Use `POST /api/rules` to define a logic-based rule.
Example payload:
```json
{
  "name": "High Liability Check",
  "logicType": "GROOVY",
  "logicContent": "contract.amount > 1000000 && !contract.liabilityCapDefined",
  "severity": "HIGH"
}
```

## 2. Triggering Evaluation
Use `POST /api/evaluations` to start an async job.
```json
{
  "contractId": "550e8400-e29b-41d4-a716-446655440000"
}
```

## 3. Resolving Problems
1. Fetch problems: `GET /api/problems?contractId=...`
2. View localization: Problem detail includes `location_in_contract`.
3. Update status: `PUT /api/problems/{id}` with `{"status": "RESOLVED"}`.

## 4. Frontend Integration
The "Cockpit" UI uses a resizable split-pane layout:
- Left Pane: `PdfViewer` component listening for `on-problem-click`.
- Right Pane: `ProblemList` component emitting `on-problem-click` with coordinate data.
- Highlighting: Use `pdf.js` to draw a transparent SVG rectangle over the coordinates provided in `location_in_contract`.
