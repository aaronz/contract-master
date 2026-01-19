<template>
  <el-container class="layout-container">
    <el-aside width="280px" class="sidebar glass">
        <div class="logo">
          <div class="logo-icon">
          <div class="logo-square"></div>
          <div class="logo-circle"></div>
        </div>
          <span class="logo-text">{{ $t('common.appTitle') }}</span>
        </div>
      
      <div class="menu-wrapper">
        <el-menu
          active-text-color="#3B82F6"
          background-color="transparent"
          class="el-menu-vertical"
          :default-active="$route.path"
          text-color="#64748B"
          router
          :collapse-transition="false"
        >
          <!-- Core -->
          <div class="menu-group-title">{{ $t('menu.coreOperations') }}</div>
          <el-menu-item index="/dashboard">
            <el-icon><DataLine /></el-icon>
            <span>{{ $t('common.dashboard') }}</span>
          </el-menu-item>
          <el-menu-item index="/contracts">
            <el-icon><Document /></el-icon>
            <span>{{ $t('common.contracts') }}</span>
          </el-menu-item>

          <!-- Integrations -->
          <div class="menu-group-title">{{ $t('menu.integrationsHub') }}</div>
          <el-sub-menu index="integrations">
            <template #title>
              <el-icon><Connection /></el-icon>
              <span>{{ $t('menu.connectors') }}</span>
            </template>
            <el-menu-item index="/integrations">{{ $t('menu.hubOverview') }}</el-menu-item>
            <el-menu-item index="/integrations/mapping">{{ $t('menu.fieldMapping') }}</el-menu-item>
            <el-menu-item index="/integrations/webhooks">{{ $t('menu.webhooks') }}</el-menu-item>
            <el-menu-item index="/integrations/secrets">{{ $t('menu.secretsKeys') }}</el-menu-item>
          </el-sub-menu>

          <!-- Risk & Compliance -->
          <div class="menu-group-title">{{ $t('menu.riskCompliance') }}</div>
          <el-menu-item index="/compliance/problems">
            <el-icon><Warning /></el-icon>
            <span>{{ $t('menu.problemCenter') }}</span>
          </el-menu-item>
          <el-menu-item index="/compliance/masking">
            <el-icon><Hide /></el-icon>
            <span>{{ $t('menu.dataMasking') }}</span>
          </el-menu-item>
          <el-menu-item index="/compliance/audit">
            <el-icon><DocumentChecked /></el-icon>
            <span>{{ $t('menu.auditLogs') }}</span>
          </el-menu-item>

          <!-- Configuration -->
          <div class="menu-group-title">{{ $t('menu.configuration') }}</div>
          <el-sub-menu index="rules">
            <template #title>
              <el-icon><Operation /></el-icon>
              <span>{{ $t('menu.ruleEngine') }}</span>
            </template>
            <el-menu-item index="/rules/list">{{ $t('menu.evaluationRules') }}</el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="settings">
            <template #title>
              <el-icon><Setting /></el-icon>
              <span>{{ $t('menu.systemSettings') }}</span>
            </template>
            <el-menu-item index="/settings/roles">{{ $t('menu.roleManagement') }}</el-menu-item>
            <el-menu-item index="/settings/users">User Management</el-menu-item>
            <el-menu-item index="/settings/permissions">{{ $t('menu.permissionMatrix') }}</el-menu-item>
            <el-menu-item index="/settings/fields">{{ $t('menu.fieldConfig') }}</el-menu-item>
          </el-sub-menu>
          
          <!-- Developer Tools -->
          <div class="menu-group-title">{{ $t('menu.developer') }}</div>
          <el-menu-item index="/developer/card-generator">
            <el-icon><Tools /></el-icon>
            <span>{{ $t('menu.cardGenerator') }}</span>
          </el-menu-item>

        </el-menu>
      </div>

      <div class="sidebar-footer">
        <div class="user-profile-mini glass-panel-sm">
          <el-avatar :size="36" src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png" />
          <div class="user-info-mini">
            <div class="user-name">Admin User</div>
            <div class="user-role">System Admin</div>
            <div class="tenant-name" style="font-size: 10px; color: #94A3B8;">{{ tenantId }}</div>
          </div>
          <el-icon class="profile-action"><Setting /></el-icon>
        </div>
      </div>
    </el-aside>
    
    <el-container class="main-container">
      <el-header class="header glass-blur">
        <div class="header-content">
          <div class="header-left">
            <el-breadcrumb separator="/">
              <el-breadcrumb-item :to="{ path: '/' }">Home</el-breadcrumb-item>
              <el-breadcrumb-item>{{ $route.name }}</el-breadcrumb-item>
            </el-breadcrumb>
          </div>
          
          <div class="header-actions">
            <el-dropdown @command="handleLangChange" trigger="click">
              <el-button circle text class="icon-btn">
                <el-icon><Operation /></el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="en" :disabled="currentLang === 'en'">English</el-dropdown-item>
                  <el-dropdown-item command="zh" :disabled="currentLang === 'zh'">中文</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>

            <el-divider direction="vertical" />
            
            <el-button circle text class="icon-btn" @click="$router.push('/guide')">
              <el-icon><QuestionFilled /></el-icon>
            </el-button>

            <el-button circle text class="icon-btn" @click="$router.push('/releases')">
              <el-icon><Timer /></el-icon>
            </el-button>

            <el-dropdown trigger="click" popper-class="notification-popper">
              <el-badge :value="unreadCount" :hidden="unreadCount === 0" class="notification-badge" type="danger">
                <el-button circle text class="icon-btn">
                  <el-icon><Bell /></el-icon>
                </el-button>
              </el-badge>
              <template #dropdown>
                <div class="notification-dropdown glass-card">
                    <div class="dropdown-header">
                      <span>{{ $t('common.notifications') }} ({{ unreadCount }})</span>
                      <el-button link type="primary" size="small" @click="handleMarkAllRead" v-if="unreadCount > 0">{{ $t('common.markAllRead') }}</el-button>
                    </div>
                    <div class="notify-list" v-loading="notifLoading">
                      <div v-if="notifications.length === 0" class="empty-notif">
                        {{ $t('common.noNotifications') }}
                      </div>
                    <div v-for="item in notifications" :key="item.id" class="notify-item" :class="{ unread: !item.isRead }" @click="handleReadNotif(item)">
                       <div class="notify-icon" :class="item.type">
                         <el-icon v-if="item.type === 'COMPLIANCE_ALERT'"><Warning /></el-icon>
                         <el-icon v-else><InfoFilled /></el-icon>
                       </div>
                       <div class="notify-content">
                         <div class="notif-title-row">
                           <b>{{ item.title }}</b>
                           <span class="notif-time">{{ formatTime(item.createTime) }}</span>
                         </div>
                         <p>{{ item.content }}</p>
                       </div>
                    </div>
                  </div>
                </div>
              </template>
            </el-dropdown>
          </div>
        </div>
      </el-header>
      
      <el-main class="main-content-wrapper">
        <router-view v-slot="{ Component }">
          <transition name="fade-slide" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { 
  DataLine, Document, Setting, Bell, InfoFilled, 
  Warning, Connection, Hide, DocumentChecked, Operation, Tools,
  QuestionFilled
} from '@element-plus/icons-vue'
import { ref, onMounted, computed, onUnmounted } from 'vue'
import { useI18n } from 'vue-i18n'
import request from '@/utils/request'

const { locale } = useI18n()
const currentLang = computed(() => locale.value)

const handleLangChange = (lang) => {
  locale.value = lang
  localStorage.setItem('lang', lang)
  location.reload() // Reload to apply Element Plus locale if needed, or just let i18n handle it
}

const tenantId = ref('default-tenant')
const notifications = ref([])
const notifLoading = ref(false)
const unreadCount = computed(() => notifications.value.filter(n => !n.isRead).length)
let notifTimer = null

const fetchNotifications = async () => {
  try {
    const res = await request.get('/notifications/my')
    notifications.value = res.data.data || []
  } catch (error) {
    console.error('Failed to fetch notifications', error)
  }
}

const handleReadNotif = async (item) => {
  if (item.isRead) return
  try {
    await request.post(`/notifications/${item.id}/read`)
    item.isRead = true
  } catch (error) {
    console.error('Failed to mark notification as read', error)
  }
}

const handleMarkAllRead = async () => {
  // Simple implementation: sequential calls or backend enhancement needed
  // For now, mark local and attempt sequentially
  for (const n of notifications.value) {
    if (!n.isRead) await handleReadNotif(n)
  }
}

const formatTime = (timeStr) => {
  if (!timeStr) return ''
  const date = new Date(timeStr)
  return date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
}

onMounted(() => {
  tenantId.value = localStorage.getItem('tenantId') || 'default-tenant'
  fetchNotifications()
  // Poll every 30s
  notifTimer = setInterval(fetchNotifications, 30000)
})

onUnmounted(() => {
  if (notifTimer) clearInterval(notifTimer)
})
</script>

<style scoped>
.layout-container {
  height: 100vh;
  background-color: var(--bg-color);
  background-image: 
    radial-gradient(at 0% 0%, hsla(253,16%,7%,0) 0, transparent 50%), 
    radial-gradient(at 50% 0%, hsla(225,39%,30%,0) 0, transparent 50%), 
    radial-gradient(at 100% 0%, hsla(339,49%,30%,0) 0, transparent 50%);
}

.glass {
  background: rgba(255, 255, 255, 0.7);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  border-right: 1px solid rgba(255, 255, 255, 0.5);
}

.sidebar {
  display: flex;
  flex-direction: column;
  transition: width 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  z-index: 100;
  box-shadow: 2px 0 20px rgba(0,0,0,0.05);
}

.logo {
  height: 90px;
  display: flex;
  align-items: center;
  padding: 0 28px;
  gap: 16px;
}

.logo-icon {
  position: relative;
  width: 36px;
  height: 36px;
}

.logo-square {
  position: absolute;
  top: 0;
  left: 0;
  width: 24px;
  height: 24px;
  background: linear-gradient(135deg, #3B82F6, #2563EB);
  border-radius: 8px;
  box-shadow: 0 4px 10px rgba(59, 130, 246, 0.4);
}

.logo-circle {
  position: absolute;
  bottom: 0;
  right: 0;
  width: 20px;
  height: 20px;
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(4px);
  border-radius: 50%;
  border: 2px solid #fff;
}

.logo-text {
  font-size: 20px;
  font-weight: 800;
  letter-spacing: -0.5px;
  background: linear-gradient(135deg, #1E293B, #475569);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.menu-wrapper {
  flex: 1;
  padding: 10px 16px;
  overflow-y: auto;
}

.menu-group-title {
  padding: 16px 12px 8px;
  font-size: 11px;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 1.2px;
  color: #94A3B8;
}

.el-menu-vertical {
  border-right: none;
}

:deep(.el-menu-item), :deep(.el-sub-menu__title) {
  height: 44px;
  margin: 4px 0;
  border-radius: 12px;
  font-weight: 500;
  color: #64748B;
}

:deep(.el-menu-item.is-active) {
  background: linear-gradient(90deg, rgba(59, 130, 246, 0.1), rgba(59, 130, 246, 0.05));
  color: #2563EB;
  font-weight: 600;
}

:deep(.el-menu-item:hover), :deep(.el-sub-menu__title:hover) {
  background-color: rgba(0, 0, 0, 0.03);
  color: #1E293B;
}

.sidebar-footer {
  padding: 24px;
  border-top: 1px solid rgba(0,0,0,0.05);
}

.glass-panel-sm {
  background: rgba(255, 255, 255, 0.5);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.5);
  border-radius: 12px;
}

.user-profile-mini {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 10px;
  cursor: pointer;
  transition: all 0.2s;
}

.user-profile-mini:hover {
  background: rgba(255, 255, 255, 0.8);
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.05);
}

.user-info-mini {
  flex: 1;
}

.user-name {
  color: #1E293B;
  font-size: 14px;
  font-weight: 600;
}

.user-role {
  color: #64748B;
  font-size: 12px;
}

.profile-action {
  color: #94A3B8;
}

/* Header */
.glass-blur {
  background: rgba(255, 255, 255, 0.5);
  backdrop-filter: blur(16px);
  -webkit-backdrop-filter: blur(16px);
  border-bottom: 1px solid rgba(255, 255, 255, 0.5);
  position: sticky;
  top: 0;
  z-index: 50;
}

.header {
  height: 70px;
  padding: 0 40px;
  display: flex;
  align-items: center;
}

.header-content {
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.main-content-wrapper {
  padding: 32px 40px;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 16px;
}

.notification-dropdown {
  width: 320px;
  padding: 0;
  overflow: hidden;
}

.dropdown-header {
  padding: 16px;
  border-bottom: 1px solid var(--border-color);
  font-weight: 600;
  background: rgba(248, 250, 252, 0.5);
}

.notify-list {
  max-height: 400px;
  overflow-y: auto;
}

.notify-item {
  display: flex;
  gap: 12px;
  padding: 16px;
  cursor: pointer;
  transition: background 0.2s;
  border-bottom: 1px solid rgba(0,0,0,0.02);
  position: relative;
}

.notify-item.unread {
  background: rgba(59, 130, 246, 0.03);
}

.notify-item.unread::before {
  content: '';
  position: absolute;
  left: 0;
  top: 0;
  bottom: 0;
  width: 3px;
  background: var(--accent-color);
}

.notify-item:hover {
  background: rgba(59, 130, 246, 0.05);
}

.notify-icon.COMPLIANCE_ALERT {
  background: #FEF2F2;
  color: #EF4444;
}

.notif-title-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 4px;
}

.notif-time {
  font-size: 10px;
  color: #94A3B8;
}

.empty-notif {
  padding: 32px;
  text-align: center;
  color: #94A3B8;
  font-size: 13px;
}

.notify-icon {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background: #EFF6FF;
  color: #3B82F6;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
}

.notify-content b {
  display: block;
  font-size: 13px;
  color: #1E293B;
  margin-bottom: 2px;
}

.notify-content p {
  margin: 0;
  font-size: 12px;
  color: #64748B;
  line-height: 1.4;
}

/* Custom transitions and other styles... */
.fade-slide-enter-active,
.fade-slide-leave-active {
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
}

.fade-slide-enter-from {
  opacity: 0;
  transform: translateY(20px) scale(0.98);
}

.fade-slide-leave-to {
  opacity: 0;
  transform: translateY(-20px) scale(0.98);
}
</style>
