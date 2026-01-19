<template>
  <div class="releases-page">
    <div class="releases-header glass-blur">
      <div class="header-content">
        <h1>{{ $t('releases.title') }}</h1>
        <p>{{ $t('releases.subtitle') }}</p>
      </div>
    </div>

    <div class="releases-container">
      <div class="timeline">
        <div v-for="(release, index) in releases" :key="index" class="release-card glass-card">
          <div class="release-header">
            <div class="version-tag">{{ release.version }}</div>
            <div class="release-date">{{ release.date }}</div>
          </div>
          <h2 class="release-title">{{ release.title }}</h2>
          
          <div class="release-section">
            <h3><el-icon><StarFilled /></el-icon> New Features</h3>
            <ul>
              <li v-for="(feature, fIdx) in release.features" :key="fIdx">{{ feature }}</li>
            </ul>
          </div>

          <div v-if="release.fixes && release.fixes.length > 0" class="release-section fixes">
            <h3><el-icon><Tools /></el-icon> Bug Fixes</h3>
            <ul>
              <li v-for="(fix, bIdx) in release.fixes" :key="bIdx">{{ fix }}</li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useI18n } from 'vue-i18n'
import { StarFilled, Tools } from '@element-plus/icons-vue'

const { tm } = useI18n()

const releases = computed(() => {
  // Use tm to get the array from i18n
  return tm('releases.items')
})
</script>

<style scoped>
.releases-page {
  min-height: 100vh;
  background: #f8fafc;
}

.releases-header {
  padding: 60px 0;
  text-align: center;
  margin-bottom: 40px;
}

.header-content h1 {
  font-size: 42px;
  font-weight: 800;
  color: #1e293b;
  margin: 0 0 12px 0;
}

.header-content p {
  font-size: 18px;
  color: #64748b;
}

.releases-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 0 32px 100px;
}

.release-card {
  padding: 40px;
  margin-bottom: 40px;
  position: relative;
}

.release-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.version-tag {
  background: #3b82f6;
  color: white;
  padding: 4px 12px;
  border-radius: 20px;
  font-weight: 700;
  font-size: 14px;
}

.release-date {
  color: #94a3b8;
  font-size: 14px;
  font-weight: 500;
}

.release-title {
  font-size: 24px;
  font-weight: 700;
  color: #1e293b;
  margin: 0 0 32px 0;
}

.release-section {
  margin-bottom: 24px;
}

.release-section h3 {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 16px;
  font-weight: 700;
  color: #334155;
  margin-bottom: 16px;
}

.release-section ul {
  padding-left: 20px;
  margin: 0;
}

.release-section li {
  color: #475569;
  line-height: 1.6;
  margin-bottom: 8px;
  position: relative;
}

.release-section.fixes h3 {
  color: #e11d48;
}

.glass-blur {
  background: rgba(255, 255, 255, 0.4);
  backdrop-filter: blur(12px);
  border-bottom: 1px solid rgba(255,255,255,0.2);
}

.glass-card {
  background: rgba(255, 255, 255, 0.7);
  backdrop-filter: blur(20px);
  border-radius: 24px;
  border: 1px solid rgba(255,255,255,0.4);
  box-shadow: 0 10px 25px rgba(0,0,0,0.03);
}
</style>
