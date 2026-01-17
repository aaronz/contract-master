# Quickstart: Manual Rule Evaluation

**Feature**: Manual Rule Evaluation
**Date**: 2026-01-16

This document provides a brief guide on how to test and verify the "Manual Rule Evaluation" feature once it is implemented.

## Prerequisites

-   You must be logged in as a user with administrator privileges for the rule engine.
-   There should be at least one rule and one contract existing in the system for your tenant.

## How to Test

### 1. Triggering a Single Rule

1.  **Navigate** to the rule editor page for a specific rule.
2.  **Locate** the "Trigger Evaluation" button on the page.
3.  **Click** the "Trigger Evaluation" button. A modal window should appear.
4.  In the modal, **select** one or more contracts from the list to use as the evaluation context.
5.  **Click** the "Run Evaluation" button in the modal.
6.  You should see a confirmation that the evaluation job has started.

### 2. Triggering a Batch of Rules

1.  **Navigate** to the main rule list page.
2.  **Select the checkboxes** next to two or more rules you wish to evaluate.
3.  **Locate** and **click** the "Trigger Batch Evaluation" button that appears after you make a selection.
4.  A modal window will appear. **Select** one or more contracts to use as the context for the batch evaluation.
5.  **Click** the "Run Evaluation" button.
6.  You should receive a confirmation that the batch evaluation job has been queued.

### 3. Verifying the Results

1.  After triggering an evaluation (single or batch), **navigate** to the "Problem Center" page using the main application navigation.
2.  On the Problem Center page, you should see a list of recent evaluation jobs. Your job should be at the top of the list.
3.  The job's status will initially be `PENDING` or `IN_PROGRESS`. After a short time, it should change to `COMPLETED`.
4.  **Click** on the completed job to view its detailed results.
5.  You will see a list of each rule-contract pair that was evaluated, along with the `PASS`, `FAIL`, or `ERROR` status for each.

### 4. Viewing Automatic Triggers

1.  Navigate to the rule list or rule editor page.
2.  Find and click the "Help" or "Documentation" icon.
3.  A section or page should appear that clearly explains the conditions under which rules are evaluated automatically by the system.
