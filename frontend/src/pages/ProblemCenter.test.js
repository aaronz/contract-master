// frontend/src/pages/ProblemCenter.test.js
import { mount } from '@vue/test-utils';
import { describe, it, expect, beforeEach, vi } from 'vitest';
import ProblemCenter from './ProblemCenter.vue';
import evaluationApi from '../services/evaluationApi';
import { ElMessage, ElTable, ElPagination, ElDialog } from 'element-plus';
import { nextTick } from 'vue';

// Mock API calls
vi.mock('../services/evaluationApi', () => ({
  default: {
    getEvaluationJobs: vi.fn(),
    getEvaluationResults: vi.fn(),
  },
}));

// Mock Element Plus Message
vi.mock('element-plus', async (importOriginal) => {
  const mod = await importOriginal();
  return {
    ...mod,
    ElMessage: {
      error: vi.fn(),
    },
  };
});


describe('ProblemCenter.vue', () => {
  const mockEvaluationJobs = [
    { id: 'job1', status: 'COMPLETED', triggerType: 'MANUAL', createdAt: '2026-01-15T10:00:00Z', completedAt: '2026-01-15T10:05:00Z', triggeredBy: 'user1' },
    { id: 'job2', status: 'PENDING', triggerType: 'AUTOMATIC', createdAt: '2026-01-15T11:00:00Z', completedAt: null, triggeredBy: 'system' },
  ];

  const mockEvaluationResults = [
    { id: 'res1', jobId: 'job1', ruleId: 'ruleA', contractId: 'contractX', status: 'PASS', details: 'Passed', timestamp: '2026-01-15T10:01:00Z' },
    { id: 'res2', jobId: 'job1', ruleId: 'ruleB', contractId: 'contractY', status: 'FAIL', details: 'Failed on condition 2', timestamp: '2026-01-15T10:02:00Z' },
  ];

  beforeEach(() => {
    vi.clearAllMocks();
    evaluationApi.getEvaluationJobs.mockResolvedValue({ data: mockEvaluationJobs });
    evaluationApi.getEvaluationResults.mockResolvedValue({ data: mockEvaluationResults });
  });

  it('renders correctly and fetches jobs on mount', async () => {
    const wrapper = mount(ProblemCenter, {
      global: {
        components: { ElTable, ElPagination, ElDialog },
      },
    });
    expect(wrapper.text()).toContain('Problem Center');
    expect(evaluationApi.getEvaluationJobs).toHaveBeenCalledTimes(1);
    await nextTick();
    expect(wrapper.findAllComponents(ElTable).length).toBe(1);
    expect(wrapper.findAll('.el-table__row').length).toBe(mockEvaluationJobs.length);
    expect(wrapper.text()).toContain('job1');
    expect(wrapper.text()).toContain('COMPLETED');
  });

  it('opens dialog and fetches results when a job is clicked', async () => {
    const wrapper = mount(ProblemCenter, {
      global: {
        components: { ElTable, ElPagination, ElDialog },
      },
    });
    await nextTick(); // Wait for jobs to load

    const firstJobRow = wrapper.findAll('.el-table__row')[0];
    await firstJobRow.trigger('click');
    
    await nextTick(); // Wait for dialog to open and results to fetch

    expect(evaluationApi.getEvaluationResults).toHaveBeenCalledWith('job1');
    expect(wrapper.findComponent(ElDialog).props().modelValue).toBe(true);
    expect(wrapper.text()).toContain('Details for Job ID: job1');
    expect(wrapper.text()).toContain('Passed');
    expect(wrapper.text()).toContain('Failed on condition 2');
  });

  it('handles pagination changes', async () => {
    const wrapper = mount(ProblemCenter, {
      global: {
        components: { ElTable, ElPagination, ElDialog },
      },
    });
    await nextTick();

    const pagination = wrapper.findComponent(ElPagination);
    await pagination.vm.$emit('current-change', 2); // Simulate changing to page 2

    expect(evaluationApi.getEvaluationJobs).toHaveBeenCalledWith(1, 20); // page index 1 (0-indexed)
  });

  it('shows error message if fetching jobs fails', async () => {
    evaluationApi.getEvaluationJobs.mockRejectedValue(new Error('Network error'));
    mount(ProblemCenter, {
      global: {
        components: { ElTable, ElPagination, ElDialog },
      },
    });
    await nextTick();
    expect(ElMessage.error).toHaveBeenCalledWith('Failed to fetch evaluation jobs.');
  });

  it('shows error message if fetching results fails', async () => {
    evaluationApi.getEvaluationResults.mockRejectedValue(new Error('Results error'));
    const wrapper = mount(ProblemCenter, {
      global: {
        components: { ElTable, ElPagination, ElDialog },
      },
    },
    );
    await nextTick(); 
    
    const firstJobRow = wrapper.findAll('.el-table__row')[0];
    await firstJobRow.trigger('click');
    await nextTick();
    
    expect(ElMessage.error).toHaveBeenCalledWith('Failed to fetch results for job job1.');
  });
});
