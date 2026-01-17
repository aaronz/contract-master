// frontend/src/pages/RuleEditor.test.js
import { mount } from '@vue/test-utils';
import { describe, it, expect, beforeEach, vi } from 'vitest';
import RuleEditor from './RuleEditor.vue';
import ContractSelectorModal from '../components/ContractSelectorModal.vue';
import evaluationApi from '../services/evaluationApi';
import { ElMessage, ElButton } from 'element-plus';
import { nextTick } from 'vue';

// Mock API calls
vi.mock('../services/evaluationApi', () => ({
  default: {
    getContracts: vi.fn(),
    triggerEvaluation: vi.fn(),
  },
}));

// Mock Element Plus Message
vi.mock('element-plus', async (importOriginal) => {
  const mod = await importOriginal();
  return {
    ...mod,
    ElMessage: {
      success: vi.fn(),
      error: vi.fn(),
    },
  };
});

describe('RuleEditor.vue', () => {
  const mockContracts = [
    { id: 'contract1', name: 'Contract One' },
    { id: 'contract2', name: 'Contract Two' },
  ];
  const mockJobId = 'job-abc-123';

  beforeEach(() => {
    vi.clearAllMocks();
    evaluationApi.getContracts.mockResolvedValue({ data: mockContracts });
    evaluationApi.triggerEvaluation.mockResolvedValue({ data: { job_id: mockJobId } });
  });

  it('renders correctly and fetches contracts on mount', async () => {
    const wrapper = mount(RuleEditor, {
      global: {
        components: { ElButton, ContractSelectorModal },
      },
    });
    expect(wrapper.text()).toContain('Rule Editor');
    expect(evaluationApi.getContracts).toHaveBeenCalledTimes(1);
    await nextTick();
    expect(wrapper.findComponent(ContractSelectorModal).props().contracts).toEqual(mockContracts);
  });

  it('opens contract selector modal when "Trigger Evaluation" button is clicked', async () => {
    const wrapper = mount(RuleEditor, {
      global: {
        components: { ElButton, ContractSelectorModal },
      },
    });
    await nextTick();

    const triggerButton = wrapper.findComponent(ElButton);
    await triggerButton.trigger('click');

    expect(wrapper.findComponent(ContractSelectorModal).props().visible).toBe(true);
  });

  it('triggers evaluation and shows success message when contracts are selected', async () => {
    const wrapper = mount(RuleEditor, {
      global: {
        components: { ElButton, ContractSelectorModal },
      },
    });
    await nextTick();

    // Open modal
    await wrapper.findComponent(ElButton).trigger('click');
    await nextTick();

    // Simulate contract selection and confirmation
    const selectedContractIds = ['contract1', 'contract2'];
    wrapper.findComponent(ContractSelectorModal).vm.$emit('confirm', selectedContractIds);
    await nextTick();

    expect(evaluationApi.triggerEvaluation).toHaveBeenCalledWith(['rule-123'], selectedContractIds);
    expect(ElMessage.success).toHaveBeenCalledWith(`Evaluation job ${mockJobId} started.`);
    expect(wrapper.findComponent(ContractSelectorModal).props().visible).toBe(false); // Modal should close
  });

  it('shows error message if fetching contracts fails', async () => {
    evaluationApi.getContracts.mockRejectedValue(new Error('Network error'));
    mount(RuleEditor, {
      global: {
        components: { ElButton, ContractSelectorModal },
      },
    });
    await nextTick();
    expect(ElMessage.error).toHaveBeenCalledWith('Failed to fetch contracts.');
  });

  it('shows error message if triggering evaluation fails', async () => {
    evaluationApi.triggerEvaluation.mockRejectedValue(new Error('API error'));
    const wrapper = mount(RuleEditor, {
      global: {
        components: { ElButton, ContractSelectorModal },
      },
    });
    await nextTick();

    await wrapper.findComponent(ElButton).trigger('click');
    await nextTick();

    wrapper.findComponent(ContractSelectorModal).vm.$emit('confirm', ['contract1']);
    await nextTick();

    expect(ElMessage.error).toHaveBeenCalledWith('Failed to trigger evaluation.');
  });
});
