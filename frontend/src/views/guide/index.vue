<template>
  <div class="guide-page">
    <div class="guide-header glass-blur">
      <div class="header-content">
        <h1>User Guide</h1>
        <p>Master the Contract Compliance & Management System</p>
      </div>
    </div>

    <div class="guide-container">
      <el-row :gutter="32">
        <el-col :span="6">
          <div class="guide-nav glass-card">
            <div 
              v-for="(item, index) in sections" 
              :key="index"
              class="nav-item"
              :class="{ active: activeSection === index }"
              @click="scrollTo(index)"
            >
              <el-icon><component :is="item.icon" /></el-icon>
              <span>{{ item.title }}</span>
            </div>
          </div>
        </el-col>

        <el-col :span="18">
          <div class="guide-content">
            <div 
              v-for="(item, index) in sections" 
              :key="index" 
              :id="'section-' + index"
              class="content-section glass-card"
            >
              <div class="section-header">
                <el-icon class="title-icon"><component :is="item.icon" /></el-icon>
                <h2>{{ item.title }}</h2>
              </div>
              <p class="section-desc">{{ item.description }}</p>
              
              <div v-if="item.steps" class="steps-box">
                <el-steps direction="vertical" :active="99">
                  <el-step 
                    v-for="(step, sIdx) in item.steps" 
                    :key="sIdx"
                    :title="step.title"
                    :description="step.content"
                  />
                </el-steps>
              </div>

              <div v-if="item.tips" class="tips-box">
                <h4><el-icon><Star /></el-icon> Pro Tips</h4>
                <ul>
                  <li v-for="(tip, tIdx) in item.tips" :key="tIdx">{{ tip }}</li>
                </ul>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { 
  Monitor, Document, DocumentChecked, PieChart, 
  Setting, Star, Connection, Search 
} from '@element-plus/icons-vue'

const activeSection = ref(0)

const sections = [
  {
    title: 'System Overview',
    icon: Monitor,
    description: 'Contract Master is an AI-powered compliance platform designed to automate contract extraction, risk evaluation, and lifecycle management.',
    tips: [
      'Use the Dashboard for a high-level view of your contract portfolio.',
      'Global search (Ctrl+K) helps you find any contract or rule instantly.'
    ]
  },
  {
    title: 'Contract Management',
    icon: Document,
    description: 'Manage your contracts from ingestion to archive.',
    steps: [
      { title: 'Ingestion', content: 'Upload PDF/Docx files or sync from downstream CRM systems.' },
      { title: 'AI Extraction', content: 'The system automatically extracts key fields (Parties, Dates, Amounts) using OCR and LLMs.' },
      { title: 'Verification', content: 'Review AI-suggested values in the Detail page and confirm them to change status to VERIFIED.' }
    ]
  },
  {
    title: 'Compliance & Rules',
    icon: DocumentChecked,
    description: 'Automated risk detection using dynamic rule engines.',
    steps: [
      { title: 'Rule Definition', content: 'Create rules using Groovy scripts or Regex in the Rule Management section.' },
      { title: 'Triggering', content: 'Rules run automatically on save, or can be triggered manually via "Re-evaluate".' },
      { title: 'Resolution', content: 'Found issues appear in the Problem Cockpit. Acknowledge or resolve them to clear the alert.' }
    ],
    tips: [
      'Groovy rules allow complex logic like "If amount > 1M, Party B must have a verified VAT ID".',
      'You can select specific rules to run if you only want to check a particular compliance aspect.'
    ]
  },
  {
    title: 'Dashboard & Analytics',
    icon: PieChart,
    description: 'Real-time statistics and visual insights.',
    tips: [
      'The Volume Trend shows your activity over time.',
      'Risk Radar categorizes threats into Legal, Financial, and Operational dimensions.'
    ]
  },
  {
    title: 'Integrations',
    icon: Connection,
    description: 'Connect Contract Master to your existing enterprise stack.',
    steps: [
      { title: 'Webhooks', content: 'Configure endpoints to receive notifications when contracts are verified or issues are found.' },
      { title: 'Field Mapping', content: 'Map internal fields to external CRM fields for seamless synchronization.' }
    ]
  }
]

const scrollTo = (index) => {
  activeSection.value = index
  const element = document.getElementById('section-' + index)
  if (element) {
    element.scrollIntoView({ behavior: 'smooth', block: 'start' })
  }
}
</script>

<style scoped>
.guide-page {
  min-height: 100vh;
  background: #f8fafc;
}

.guide-header {
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

.guide-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 32px 100px;
}

.guide-nav {
  position: sticky;
  top: 100px;
  padding: 16px;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 16px;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.2s;
  color: #64748b;
  font-weight: 600;
  margin-bottom: 4px;
}

.nav-item:hover {
  background: rgba(59, 130, 246, 0.05);
  color: #3b82f6;
}

.nav-item.active {
  background: #3b82f6;
  color: white;
  box-shadow: 0 4px 12px rgba(59, 130, 246, 0.3);
}

.content-section {
  padding: 40px;
  margin-bottom: 32px;
  scroll-margin-top: 100px;
}

.section-header {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 20px;
}

.title-icon {
  font-size: 28px;
  color: #3b82f6;
}

.section-header h2 {
  font-size: 24px;
  font-weight: 700;
  margin: 0;
  color: #1e293b;
}

.section-desc {
  font-size: 16px;
  color: #475569;
  line-height: 1.6;
  margin-bottom: 32px;
}

.steps-box {
  background: rgba(255,255,255,0.5);
  padding: 32px;
  border-radius: 16px;
  border: 1px solid rgba(0,0,0,0.05);
  margin-bottom: 32px;
}

.tips-box {
  background: #f0f9ff;
  padding: 24px;
  border-radius: 16px;
  border: 1px solid #bae6fd;
}

.tips-box h4 {
  display: flex;
  align-items: center;
  gap: 8px;
  margin: 0 0 12px 0;
  color: #0369a1;
}

.tips-box ul {
  margin: 0;
  padding-left: 20px;
  color: #0c4a6e;
}

.tips-box li {
  margin-bottom: 8px;
  font-size: 14px;
}

.glass-blur {
  background: rgba(255, 255, 255, 0.4);
  backdrop-filter: blur(12px);
  border-bottom: 1px solid rgba(255,255,255,0.2);
}

.glass-card {
  background: rgba(255, 255, 255, 0.7);
  backdrop-filter: blur(20px);
  border-radius: 20px;
  border: 1px solid rgba(255,255,255,0.4);
  box-shadow: 0 10px 25px rgba(0,0,0,0.03);
}
</style>
