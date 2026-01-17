// frontend/src/pages/RuleList.test.js
import { mount } from '@vue/test-utils';
import { describe, it, expect, beforeEach, vi } from 'vitest';
import RuleList from './RuleList.vue';
import ContractSelectorModal from '../components/ContractSelectorModal.vue';
import evaluationApi from '../services/evaluationApi';
import { ElMessage, ElButton, ElMessageBox, ElTable, ElTableColumn, ElTag } from 'element-plus';
import { nextTick } from 'vue';
import { QuestionFilled } from '@element-plus/icons-vue';


// Mock API calls
vi.mock('../services/evaluationApi', () => ({
  default: {
    getContracts: vi.fn(),
    triggerEvaluation: vi.fn(),
    getTriggerScenarios: vi.fn(),
  },
}));

// Mock Element Plus Message and MessageBox
vi.mock('element-plus', async (importOriginal) => {
  const mod = await importOriginal();
  return {
    ...mod,
    ElMessage: {
      success: vi.fn(),
      warning: vi.fn(),
      error: vi.fn(),
    },
    ElMessageBox: {
      alert: vi.fn(() => Promise.resolve()), // Mock ElMessageBox.alert to resolve immediately
    },
  };
});

describe('RuleList.vue', () => {
  const mockContracts = [
    { id: 'contract1', name: 'Contract One' },
    { id: 'contract2', name: 'Contract Two' },
  ];
  const mockRules = [
    { id: 'rule-a', name: 'Rule A - High Priority' },
    { id: 'rule-b', name: 'Rule B - Compliance Check' },
  ];
  const mockJobId = 'job-xyz-789';
  const mockScenarios = {
    scenarios: [
      { name: 'Creation', description: 'Rules run on creation' },
      { name: 'Update', description: 'Rules run on update' },
    ],
  };

  beforeEach(() => {
    vi.clearAllMocks();
    evaluationApi.getContracts.mockResolvedValue({ data: mockContracts });
    evaluationApi.triggerEvaluation.mockResolvedValue({ data: { job_id: mockJobId } });
    evaluationApi.getTriggerScenarios.mockResolvedValue({ data: mockScenarios });
  });

  it('renders correctly and fetches contracts on mount', async () => {
    const wrapper = mount(RuleList, {
      global: {
        components: { ElTable, ElTableColumn, ElButton, ContractSelectorModal, QuestionFilled, ElTag },
      },
    });
    expect(wrapper.text()).toContain('Rule List');
    expect(evaluationApi.getContracts).toHaveBeenCalledTimes(1);
    await nextTick();
    expect(wrapper.findComponent(ContractSelectorModal).props().contracts).toEqual(mockContracts);
  });

  it('batch evaluation button appears when rules are selected', async () => {
    const wrapper = mount(RuleList, {
      global: {
        components: { ElTable, ElTableColumn, ElButton, ContractSelectorModal, QuestionFilled, ElTag },
      },
    });
    await nextTick();

    expect(wrapper.find('button.el-button--primary').exists()).toBe(false); // No button initially

    // Simulate selecting rules
    const table = wrapper.findComponent(ElTable);
    table.vm.$emit('selection-change', mockRules);
    await nextTick();

    const batchButton = wrapper.find('button.el-button--primary');
    expect(batchButton.exists()).toBe(true);
    expect(batchButton.text()).toContain('Trigger Batch Evaluation (2 rules selected)');
  });

  it('triggers batch evaluation and shows success message', async () => {
    const wrapper = mount(RuleList, {
      global: {
        components: { ElTable, ElTableColumn, ElButton, ContractSelectorModal, QuestionFilled, ElTag },
      },
    });
    await nextTick();

    // Select rules
    const table = wrapper.findComponent(ElTable);
    table.vm.$emit('selection-change', mockRules);
    await nextTick();

    // Click batch button to open modal
    await wrapper.find('button.el-button--primary').trigger('click');
    await nextTick();

    // Simulate contract selection and confirmation
    const selectedContractIds = ['contract1'];
    wrapper.findComponent(ContractSelectorModal).vm.$emit('confirm', selectedContractIds);
    await nextTick();

    expect(evaluationApi.triggerEvaluation).toHaveBeenCalledWith(['rule-a', 'rule-b'], selectedContractIds);
    expect(ElMessage.success).toHaveBeenCalledWith(`Batch evaluation job ${mockJobId} started.`);
    expect(wrapper.findComponent(ContractSelectorModal).props().visible).toBe(false); // Modal should close
  });

  it('shows warning if no rules selected for batch evaluation but button somehow clicked', async () => {
    const wrapper = mount(RuleList, {
      global: {
        components: { ElTable, ElTableColumn, ElButton, ContractSelectorModal, QuestionFilled, ElTag },
      },
    });
    await nextTick();

    // No rules selected, but simulate button click (e.g., if button state was wrong)
    const batchButton = wrapper.find('button.el-button--primary'); // This won't exist but for completeness
    if (batchButton.exists()) {
        await batchButton.trigger('click');
    }
    wrapper.vm.openContractSelectorForBatch(); // Directly call the method to test logic

    expect(ElMessage.warning).toHaveBeenCalledWith('Please select rules for batch evaluation.');
    expect(wrapper.findComponent(ContractSelectorModal).props().visible).toBe(false);
  });

  it('displays trigger scenarios when help button is clicked', async () => {
    const wrapper = mount(RuleList, {
      global: {
        components: { ElTable, ElTableColumn, ElButton, ContractSelectorModal, QuestionFilled, ElTag },
      },
    });
    await nextTick();

    const helpButton = wrapper.findComponent(QuestionFilled).findComponent(ElButton);
    await helpButton.trigger('click');
    await nextTick();

    expect(evaluationApi.getTriggerScenarios).toHaveBeenCalledTimes(1);
    expect(ElMessageBox.alert).toHaveBeenCalledWith(
      expect.stringContaining('Rules run on creation'),
      'Automatic Rule Trigger Scenarios',
      expect.anything()
    );
  });

  it('shows error if fetching trigger scenarios fails', async () => {
    evaluationApi.getTriggerScenarios.mockRejectedValue(new Error('Scenario API error'));
    const wrapper = mount(RuleList, {
      global: {
        components: { ElTable, ElTableColumn, ElButton, ContractSelectorModal, QuestionFilled, ElTag },
      },
    });
    await nextTick();

    const helpButton = wrapper.findComponent(QuestionFilled).findComponent(ElButton);
    await helpButton.trigger('click');
    await nextTick();

    expect(ElMessage.error).toHaveBeenCalledWith('Failed to fetch trigger scenarios.');
  });
});
