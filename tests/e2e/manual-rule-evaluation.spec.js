// tests/e2e/manual-rule-evaluation.spec.js
import { test, expect } from '@playwright/test';

test.describe('Manual Rule Evaluation E2E Flow', () => {

  test.beforeEach(async ({ page }) => {
    // Assuming authentication is handled globally or prior to these tests
    // Navigate to a page that lists rules or allows editing a rule
    await page.goto('/rule-editor'); // Placeholder URL
  });

  test('should allow a user to trigger a single rule evaluation', async ({ page }) => {
    // Simulate initial contracts fetch success
    await page.route('**/api/contracts', route => {
      route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: JSON.stringify([
          { id: 'contract-e2e-1', name: 'E2E Test Contract 1' },
          { id: 'contract-e2e-2', name: 'E2E Test Contract 2' },
        ]),
      });
    });

    // Simulate trigger evaluation success
    await page.route('**/api/evaluations', async route => {
      await route.fulfill({
        status: 202,
        contentType: 'application/json',
        body: JSON.stringify({ job_id: 'e2e-job-1' }),
      });
    });

    // Assume RuleEditor.vue is rendered
    await expect(page.locator('h1:has-text("Rule Editor")')).toBeVisible();

    // Click the "Trigger Evaluation" button
    await page.getByRole('button', { name: 'Trigger Evaluation' }).click();

    // Expect the Contract Selector modal to appear
    const contractSelectorModal = page.locator('.el-dialog__wrapper');
    await expect(contractSelectorModal.getByText('Select Contracts')).toBeVisible();

    // Select contracts (e.g., the first two)
    await contractSelectorModal.locator('.el-table__body tr').nth(0).locator('.el-checkbox').click();
    await contractSelectorModal.locator('.el-table__body tr').nth(1).locator('.el-checkbox').click();

    // Click "Confirm" in the modal
    await contractSelectorModal.getByRole('button', { name: 'Confirm' }).click();

    // Expect a success message (e.g., Element Plus ElMessage)
    await expect(page.locator('.el-message--success')).toContainText('Evaluation job e2e-job-1 started.');

    // Verify modal is closed
    await expect(contractSelectorModal.getByText('Select Contracts')).not.toBeVisible();
  });

  test('should allow a user to trigger a batch rule evaluation', async ({ page }) => {
    // Navigate to Rule List page first
    await page.goto('/rule-list'); // Placeholder URL

    // Simulate initial contracts fetch success
    await page.route('**/api/contracts', route => {
      route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: JSON.stringify([
          { id: 'contract-e2e-batch-1', name: 'E2E Test Batch Contract 1' },
        ]),
      });
    });

    // Simulate trigger evaluation success
    await page.route('**/api/evaluations', async route => {
      await route.fulfill({
        status: 202,
        contentType: 'application/json',
        body: JSON.stringify({ job_id: 'e2e-batch-job-1' }),
      });
    });

    // Assume RuleList.vue is rendered
    await expect(page.locator('h1:has-text("Rule List")')).toBeVisible();

    // Select multiple rules (e.g., the first two mock rules)
    await page.locator('.el-table__body tr').nth(0).locator('.el-checkbox').click();
    await page.locator('.el-table__body tr').nth(1).locator('.el-checkbox').click();

    // Click the "Trigger Batch Evaluation" button
    await page.getByRole('button', { name: 'Trigger Batch Evaluation (2 rules selected)' }).click();

    // Expect the Contract Selector modal to appear
    const contractSelectorModal = page.locator('.el-dialog__wrapper');
    await expect(contractSelectorModal.getByText('Select Contracts')).toBeVisible();

    // Select contracts (e.g., the first one)
    await contractSelectorModal.locator('.el-table__body tr').nth(0).locator('.el-checkbox').click();

    // Click "Confirm" in the modal
    await contractSelectorModal.getByRole('button', { name: 'Confirm' }).click();

    // Expect a success message
    await expect(page.locator('.el-message--success')).toContainText('Batch evaluation job e2e-batch-job-1 started.');

    // Verify modal is closed
    await expect(contractSelectorModal.getByText('Select Contracts')).not.toBeVisible();
  });

  test('should allow a user to view automatic trigger scenarios', async ({ page }) => {
    await page.goto('/rule-list'); // Go to Rule List page

    // Simulate trigger scenarios fetch success
    await page.route('**/api/rules/trigger-scenarios', route => {
      route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: JSON.stringify({
          scenarios: [
            { name: 'Creation', description: 'Rules run on creation' },
            { name: 'Update', description: 'Rules run on update' },
          ],
        }),
      });
    });

    await expect(page.locator('h1:has-text("Rule List")')).toBeVisible();

    // Click the help button (assuming it's visually distinct)
    await page.getByRole('button', { name: 'QuestionFilled' }).click(); // Using accessible name for icon button

    // Expect the ElMessageBox alert to appear
    const alertBox = page.locator('.el-message-box');
    await expect(alertBox).toBeVisible();
    await expect(alertBox.locator('.el-message-box__title')).toContainText('Automatic Rule Trigger Scenarios');
    await expect(alertBox).toContainText('Rules run on creation');
    await expect(alertBox).toContainText('Rules run on update');

    // Close the alert
    await alertBox.getByRole('button', { name: 'OK' }).click();
    await expect(alertBox).not.toBeVisible();
  });

  test('should allow a user to view evaluation jobs and results in Problem Center', async ({ page }) => {
    // Simulate jobs fetch success
    await page.route('**/api/problem-center/evaluation-jobs**', route => {
      route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: JSON.stringify([
          { id: 'pc-job-1', status: 'COMPLETED', triggerType: 'MANUAL', createdAt: '2026-01-16T10:00:00Z', completedAt: '2026-01-16T10:05:00Z', triggeredBy: 'e2e-user' },
        ]),
      });
    });

    // Simulate results fetch success
    await page.route('**/api/problem-center/evaluation-jobs/pc-job-1/results', route => {
      route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: JSON.stringify([
          { id: 'pc-res-1', jobId: 'pc-job-1', ruleId: 'pc-rule-1', contractId: 'pc-contract-1', status: 'PASS', details: 'E2E result passed', timestamp: '2026-01-16T10:01:00Z' },
        ]),
      });
    });

    await page.goto('/problem-center'); // Navigate to Problem Center page

    await expect(page.locator('h1:has-text("Problem Center")')).toBeVisible();
    await expect(page.locator('.el-table__body')).toContainText('pc-job-1');
    await expect(page.locator('.el-table__body')).toContainText('COMPLETED');

    // Click on the job to view details
    await page.locator('.el-table__body tr').nth(0).click();

    const jobDetailsDialog = page.locator('.el-dialog__wrapper');
    await expect(jobDetailsDialog).toBeVisible();
    await expect(jobDetailsDialog).toContainText('Details for Job ID: pc-job-1');
    await expect(jobDetailsDialog).toContainText('pc-rule-1');
    await expect(jobDetailsDialog).toContainText('pc-contract-1');
    await expect(jobDetailsDialog).toContainText('PASS');
    await expect(jobDetailsDialog).toContainText('E2E result passed');

    // Close the dialog
    await jobDetailsDialog.getByRole('button', { name: 'Close' }).click();
    await expect(jobDetailsDialog).not.toBeVisible();
  });
});
