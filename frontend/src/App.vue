<template>
  <el-config-provider>
    <router-view />
  </el-config-provider>
</template>

<script setup>
import { watch, onMounted } from 'vue'
import { useI18n } from 'vue-i18n'

const { t, locale } = useI18n()

watch(locale, (newLocale) => {
  document.title = t('common.appTitle')
})

onMounted(() => {
  document.title = t('common.appTitle')
})
</script>

<style>
#app {
  height: 100vh;
  margin: 0;
  padding: 0;
}
</style>
