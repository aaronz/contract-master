import { shallowMount } from '@vue/test-utils';
import { beforeEach, describe, expect, it, vi } from 'vitest';
import RuleSelectorModal from '@/components/RuleSelectorModal.vue';
import evaluationApi from '@/services/evaluationApi'; // Mock this API
import { ElMessage } from 'element-plus'; // Mock ElMessage

// Mock Element Plus components and ElMessage
vi.mock('element-plus', () => ({
  ElMessage: {
    error: vi.fn(),
    warning: vi.fn(),
    success: vi.fn(),
  },
  ElDialog: { template: '<div><slot></slot><slot name="footer"></slot></div>' },
  ElInput: { template: '<input />' },
  ElIcon: { template: '<span></span>' },
  ElTable: { template: '<table><slot></slot></table>' },
  ElTableColumn: { template: '<td></td>' },
  ElButton: { template: '<button><slot></slot></button>' },
}));

// Mock evaluationApi
vi.mock('@/services/evaluationApi', () => ({
  default: {
    getRules: vi.fn(),
  },
}));

describe('RuleSelectorModal.vue', () => {
  let wrapper;
  const mockRules = [
    { ruleId: 'r1', ruleName: 'Rule One', ruleType: 'TypeA', ruleLevel: 'High' },
    { ruleId: 'r2', ruleName: 'Rule Two', ruleType: 'TypeB', ruleLevel: 'Medium' },
  ];

  beforeEach(() => {
    vi.resetAllMocks();
    evaluationApi.getRules.mockResolvedValue({ data: { data: mockRules } }); // Mock successful API call
  });

  it('renders correctly when modelValue is true', async () => {
    wrapper = shallowMount(RuleSelectorModal, {
      props: { modelValue: true },
      global: {
        stubs: ['el-dialog', 'el-input', 'el-icon', 'el-table', 'el-table-column', 'el-button'],
      },
    });
    await wrapper.vm.$nextTick(); // Wait for component to update

    expect(wrapper.find('el-dialog').exists()).toBe(true);
    expect(evaluationApi.getRules).toHaveBeenCalledTimes(1);
    expect(wrapper.vm.rules).toEqual(mockRules);
  });

  it('does not fetch rules when modelValue is false initially', () => {
    wrapper = shallowMount(RuleSelectorModal, {
      props: { modelValue: false },
      global: {
        stubs: ['el-dialog', 'el-input', 'el-icon', 'el-table', 'el-table-column', 'el-button'],
      },
    });

    expect(wrapper.find('el-dialog').exists()).toBe(true); // ElDialog is always rendered but might be hidden
    expect(evaluationApi.getRules).not.toHaveBeenCalled();
  });

  it('fetches rules when modelValue changes from false to true', async () => {
    wrapper = shallowMount(RuleSelectorModal, {
      props: { modelValue: false },
      global: {
        stubs: ['el-dialog', 'el-input', 'el-icon', 'el-table', 'el-table-column', 'el-button'],
      },
    });

    await wrapper.setProps({ modelValue: true });
    await wrapper.vm.$nextTick(); // Wait for component to update

    expect(evaluationApi.getRules).toHaveBeenCalledTimes(1);
    expect(wrapper.vm.rules).toEqual(mockRules);
  });

  it('displays error message if fetching rules fails', async () => {
    evaluationApi.getRules.mockRejectedValue(new Error('API Error'));
    wrapper = shallowMount(RuleSelectorModal, {
      props: { modelValue: true },
      global: {
        stubs: ['el-dialog', 'el-input', 'el-icon', 'el-table', 'el-table-column', 'el-button'],
      },
    });
    await wrapper.vm.$nextTick(); // Wait for component to update

    expect(evaluationApi.getRules).toHaveBeenCalledTimes(1);
    expect(ElMessage.error).toHaveBeenCalledWith('Failed to load rules');
  });

  it('emits confirm event with selected rule IDs when confirmed', async () => {
    wrapper = shallowMount(RuleSelectorModal, {
      props: { modelValue: true },
      global: {
        stubs: ['el-dialog', 'el-input', 'el-icon', 'el-table', 'el-table-column', 'el-button'],
      },
    });
    await wrapper.vm.$nextTick();

    // Simulate selection change in el-table
    wrapper.vm.handleSelectionChange([{ ruleId: 'r1' }]);
    expect(wrapper.vm.multipleSelection).toEqual([{ ruleId: 'r1' }]);

    // Find the confirm button and click it
    await wrapper.find('button').trigger('click'); // Assuming first button is Cancel, second is Confirm

    // Check emitted event
    expect(wrapper.emitted('confirm')).toBeTruthy();
    expect(wrapper.emitted('confirm')[0][0]).toEqual(['r1']);
    expect(ElMessage.warning).not.toHaveBeenCalled();
    expect(wrapper.emitted('update:modelValue')).toBeTruthy(); // Should close modal
    expect(wrapper.emitted('update:modelValue')[0][0]).toBe(false);
  });

  it('shows warning if no rules are selected on confirm', async () => {
    wrapper = shallowMount(RuleSelectorModal, {
      props: { modelValue: true },
      global: {
        stubs: ['el-dialog', 'el-input', 'el-icon', 'el-table', 'el-table-column', 'el-button'],
      },
    });
    await wrapper.vm.$nextTick();

    wrapper.vm.handleSelectionChange([]); // No selection
    await wrapper.find('button').trigger('click'); // Click confirm button

    expect(ElMessage.warning).toHaveBeenCalledWith('Please select at least one rule.');
    expect(wrapper.emitted('confirm')).toBeFalsy(); // Confirm should not be emitted
  });

  it('emits update:modelValue(false) when cancel button is clicked', async () => {
    wrapper = shallowMount(RuleSelectorModal, {
      props: { modelValue: true },
      global: {
        stubs: ['el-dialog', 'el-input', 'el-icon', 'el-table', 'el-table-column', 'el-button'],
      },
    });
    await wrapper.vm.$nextTick();

    // Find the cancel button (assuming it's the first button)
    const cancelButton = wrapper.findAll('button')[0];
    await cancelButton.trigger('click');

    expect(wrapper.emitted('update:modelValue')).toBeTruthy();
    expect(wrapper.emitted('update:modelValue')[0][0]).toBe(false);
  });
});
