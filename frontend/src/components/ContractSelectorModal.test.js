// frontend/src/components/ContractSelectorModal.test.js
import { mount } from '@vue/test-utils';
import { describe, it, expect, beforeEach, vi } from 'vitest';
import ContractSelectorModal from './ContractSelectorModal.vue';
import { ElDialog, ElTable, ElTableColumn, ElButton, ElMessage } from 'element-plus'; // Import Element Plus components
import { nextTick } from 'vue';

// Mock Element Plus components globally or selectively if needed
// For simplicity, we'll mock ElMessage directly for its warning method
vi.mock('element-plus', async (importOriginal) => {
  const mod = await importOriginal();
  return {
    ...mod,
    ElMessage: {
      warning: vi.fn(),
    },
  };
});

describe('ContractSelectorModal.vue', () => {
  const contracts = [
    { id: '1', name: 'Contract A' },
    { id: '2', name: 'Contract B' },
    { id: '3', name: 'Contract C' },
  ];

  let wrapper;

  beforeEach(() => {
    wrapper = mount(ContractSelectorModal, {
      props: {
        visible: true,
        contracts: contracts,
      },
      global: {
        components: {
          ElDialog,
          ElTable,
          ElTableColumn,
          ElButton,
        },
      },
    });
  });

  it('renders correctly when visible', () => {
    expect(wrapper.findComponent(ElDialog).exists()).toBe(true);
    expect(wrapper.findComponent(ElDialog).props().modelValue).toBe(true);
    expect(wrapper.text()).toContain('Select Contracts');
    expect(wrapper.findAllComponents(ElTableColumn).length).toBe(3); // Selection, Name, ID
    expect(wrapper.findAllComponents(ElTable).length).toBe(1);
  });

  it('displays contracts in the table', () => {
    const tableRows = wrapper.findAll('.el-table__row');
    expect(tableRows.length).toBe(contracts.length);
    expect(tableRows[0].text()).toContain('Contract A');
    expect(tableRows[0].text()).toContain('1');
  });

  it('emits "update:visible" when dialog is closed', async () => {
    wrapper.findComponent(ElDialog).vm.$emit('close'); // Simulate dialog close
    await nextTick();
    expect(wrapper.emitted('update:visible')).toBeTruthy();
    expect(wrapper.emitted('update:visible')[0][0]).toBe(false);
  });

  it('emits "confirm" with selected contract IDs when confirmed', async () => {
    const table = wrapper.findComponent(ElTable);
    // Simulate selection change (e.g., selecting Contract A and B)
    const selected = [contracts[0], contracts[1]];
    table.vm.$emit('selection-change', selected);
    await nextTick();

    await wrapper.find('button.el-button--primary').trigger('click'); // Click Confirm
    expect(wrapper.emitted('confirm')).toBeTruthy();
    expect(wrapper.emitted('confirm')[0][0]).toEqual(['1', '2']);
    expect(wrapper.emitted('update:visible')[0][0]).toBe(false); // Dialog should close
  });

  it('shows a warning if no contracts are selected and confirm is clicked', async () => {
    const table = wrapper.findComponent(ElTable);
    table.vm.$emit('selection-change', []); // Simulate no selection
    await nextTick();

    await wrapper.find('button.el-button--primary').trigger('click'); // Click Confirm
    expect(ElMessage.warning).toHaveBeenCalledWith('Please select at least one contract.');
    expect(wrapper.emitted('confirm')).toBeFalsy(); // Should not emit confirm
  });
});
