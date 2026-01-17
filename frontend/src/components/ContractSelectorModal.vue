<template>
  <el-dialog
    v-model="dialogVisible"
    title="Select Contracts"
    width="50%"
    :before-close="handleClose"
  >
    <el-table
      ref="multipleTableRef"
      :data="contracts"
      style="width: 100%"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column property="name" label="Contract Name" width="200" />
      <el-table-column property="id" label="ID" />
    </el-table>

    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="confirmSelection">Confirm</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, watch } from 'vue';
import { ElMessage } from 'element-plus';

const props = defineProps({
  visible: Boolean,
  contracts: Array, // Expected format: [{ id: '1', name: 'Contract A' }]
});

const emit = defineEmits(['update:visible', 'confirm']);

const dialogVisible = ref(props.visible);
const multipleSelection = ref([]);

watch(() => props.visible, (newVal) => {
  dialogVisible.value = newVal;
});

watch(dialogVisible, (newVal) => {
  emit('update:visible', newVal);
});

const handleSelectionChange = (val) => {
  multipleSelection.value = val;
};

const handleClose = (done) => {
  emit('update:visible', false);
  done();
};

const confirmSelection = () => {
  if (multipleSelection.value.length === 0) {
    ElMessage.warning('Please select at least one contract.');
    return;
  }
  emit('confirm', multipleSelection.value.map(contract => contract.id));
  dialogVisible.value = false;
};
</script>

<style scoped>
.dialog-footer button:first-child {
  margin-right: 10px;
}
</style>
