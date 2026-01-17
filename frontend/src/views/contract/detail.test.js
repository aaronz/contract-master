import { mount, shallowMount } from '@vue/test-utils';
import { describe, it, expect, beforeEach, vi } from 'vitest';
import ContractDetail from '@/views/contract/detail.vue';
import { ElMessage, ElButton } from 'element-plus';
import { useRouter } from 'vue-router';
import { useFieldStore } from '@/stores/fieldStore';
import evaluationApi from '@/services/evaluationApi';
import RuleSelectorModal from '@/components/RuleSelectorModal.vue';

// Mock API calls
vi.mock('@/services/evaluationApi', () => ({
  default: {
    triggerEvaluation: vi.fn(),
    getRules: vi.fn(), // Needed for RuleSelectorModal
  },
}));

// Mock Element Plus Message
vi.mock('element-plus', () => ({
  ElMessage: {
    success: vi.fn(),
    error: vi.fn(),
    warning: vi.fn(),
  },
}));

// Mock Vue Router
vi.mock('vue-router', () => ({
  useRouter: vi.fn(() => ({
    currentRoute: { value: { params: { id: 'test-contract-id' } } },
    push: vi.fn(),
    back: vi.fn(),
  })),
}));

// Mock Pinia store
vi.mock('@/stores/fieldStore', () => ({
  useFieldStore: vi.fn(() => ({
    fields: [
      { fieldCode: 'contract_no', fieldName: 'Contract No', source: 'CORE' },
      { fieldCode: 'contract_name', fieldName: 'Contract Name', source: 'CORE' },
    ],
    fetchFieldConfigs: vi.fn(),
    isFieldVisible: vi.fn(() => true),
    getFieldName: vi.fn((code) => code.replace('_', ' ')),
  })),
}));

// Mock global fetch
global.fetch = vi.fn((url) => {
  if (url.includes('/api/contracts/test-contract-id')) {
    return Promise.resolve({
      ok: true,
      json: () => Promise.resolve({
        data: {
          contractId: 'test-contract-id',
          contractNo: 'CON-001',
          contractName: 'Test Contract',
          contractStatus: 'Active',
        },
      }),
    });
  }
  return Promise.reject(new Error('not found'));
});

describe('ContractDetail.vue', () => {
  const mockJobId = 'job-re-eval-123';
  const mockRules = [{ ruleId: 'r1', ruleName: 'Rule One' }];

  beforeEach(() => {
    vi.clearAllMocks();
    evaluationApi.triggerEvaluation.mockResolvedValue({ data: { jobId: mockJobId } });
    evaluationApi.getRules.mockResolvedValue({ data: { data: mockRules } }); // For RuleSelectorModal
  });

  it('renders contract details correctly on mount', async () => {
    const wrapper = mount(ContractDetail);
    await vi.waitFor(() => {
      expect(wrapper.text()).toContain('CON-001');
      expect(wrapper.text()).toContain('Test Contract');
    });
    expect(global.fetch).toHaveBeenCalledWith(
      '/api/contracts/test-contract-id',
      expect.any(Object)
    );
    expect(useFieldStore().fetchFieldConfigs).toHaveBeenCalledTimes(1);
  });

  it('opens RuleSelectorModal when "Re-evaluate" button is clicked', async () => {
    const wrapper = mount(ContractDetail);
    await wrapper.vm.$nextTick(); // Ensure component is rendered

    const reEvaluateButton = wrapper.findComponent(ElButton, { text: 'Re-evaluate' });
    await reEvaluateButton.trigger('click');

    // RuleSelectorModal is mounted, check if it's visible
    const ruleSelector = wrapper.findComponent(RuleSelectorModal);
    expect(ruleSelector.props().modelValue).toBe(true);
  });

  it('calls triggerEvaluation API and shows success message on modal confirm', async () => {
    const wrapper = mount(ContractDetail);
    await wrapper.vm.$nextTick();

    // Open modal
    const reEvaluateButton = wrapper.findComponent(ElButton, { text: 'Re-evaluate' });
    await reEvaluateButton.trigger('click');
    await wrapper.vm.$nextTick();

    // Simulate modal confirm event
    const selectedRuleIds = ['r1', 'r2'];
    wrapper.findComponent(RuleSelectorModal).vm.$emit('confirm', selectedRuleIds);
    await wrapper.vm.$nextTick();

    expect(evaluationApi.triggerEvaluation).toHaveBeenCalledWith(
      'test-contract-id',
      selectedRuleIds
    );
    expect(ElMessage.success).toHaveBeenCalledWith(
      `Re-evaluation started successfully! Job ID: ${mockJobId}`
    );
    // Ensure modal is closed
    expect(wrapper.findComponent(RuleSelectorModal).props().modelValue).toBe(false);
  });

  it('shows warning if no rules are selected for re-evaluation', async () => {
    const wrapper = mount(ContractDetail);
    await wrapper.vm.$nextTick();

    const reEvaluateButton = wrapper.findComponent(ElButton, { text: 'Re-evaluate' });
    await reEvaluateButton.trigger('click');
    await wrapper.vm.$nextTick();

    // Simulate modal confirm with empty rule list
    wrapper.findComponent(RuleSelectorModal).vm.$emit('confirm', []);
    await wrapper.vm.$nextTick();

    expect(ElMessage.warning).toHaveBeenCalledWith('No rules selected for re-evaluation.');
    expect(evaluationApi.triggerEvaluation).not.toHaveBeenCalled();
  });

  it('shows error message if re-evaluation API call fails', async () => {
    evaluationApi.triggerEvaluation.mockRejectedValue({
      response: { data: { message: 'API Error' } },
    });

    const wrapper = mount(ContractDetail);
    await wrapper.vm.$nextTick();

    const reEvaluateButton = wrapper.findComponent(ElButton, { text: 'Re-evaluate' });
    await reEvaluateButton.trigger('click');
    await wrapper.vm.$nextTick();

    wrapper.findComponent(RuleSelectorModal).vm.$emit('confirm', ['r1']);
    await wrapper.vm.$nextTick();

    expect(ElMessage.error).toHaveBeenCalledWith('Failed to start re-evaluation: API Error');
  });

  it('shows warning for conflict if re-evaluation API returns 409 status', async () => {
    evaluationApi.triggerEvaluation.mockRejectedValue({
      response: { status: 409, data: { message: 'Already in progress' } },
    });

    const wrapper = mount(ContractDetail);
    await wrapper.vm.$nextTick();

    const reEvaluateButton = wrapper.findComponent(ElButton, { text: 'Re-evaluate' });
    await reEvaluateButton.trigger('click');
    await wrapper.vm.$nextTick();

    wrapper.findComponent(RuleSelectorModal).vm.$emit('confirm', ['r1']);
    await wrapper.vm.$nextTick();

    expect(ElMessage.warning).toHaveBeenCalledWith('An evaluation is already in progress for this contract.');
  });
});
