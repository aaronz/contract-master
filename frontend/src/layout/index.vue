<template>
  <el-container class="layout-container">
    <el-aside width="280px" class="sidebar glass">
      <div class="logo">
        <div class="logo-icon">
          <div class="logo-square"></div>
          <div class="logo-circle"></div>
        </div>
        <span class="logo-text">Contract Master</span>
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
          <div class="menu-group-title">Core Operations</div>
          <el-menu-item index="/dashboard">
            <el-icon><DataLine /></el-icon>
            <span>Dashboard</span>
          </el-menu-item>
          <el-menu-item index="/contracts">
            <el-icon><Document /></el-icon>
            <span>Contract Management</span>
          </el-menu-item>

          <!-- Integrations -->
          <div class="menu-group-title">Integrations Hub</div>
          <el-sub-menu index="integrations">
            <template #title>
              <el-icon><Connection /></el-icon>
              <span>Connectors</span>
            </template>
            <el-menu-item index="/integrations">Hub Overview</el-menu-item>
            <el-menu-item index="/integrations/mapping">Field Mapping</el-menu-item>
            <el-menu-item index="/integrations/webhooks">Webhooks</el-menu-item>
            <el-menu-item index="/integrations/secrets">Secrets & Keys</el-menu-item>
          </el-sub-menu>

          <!-- Risk & Compliance -->
          <div class="menu-group-title">Risk & Compliance</div>
          <el-menu-item index="/compliance/problems">
            <el-icon><Warning /></el-icon>
            <span>Problem Center</span>
          </el-menu-item>
          <el-menu-item index="/compliance/masking">
            <el-icon><Hide /></el-icon>
            <span>Data Masking</span>
          </el-menu-item>
          <el-menu-item index="/compliance/audit">
            <el-icon><DocumentChecked /></el-icon>
            <span>Audit Logs</span>
          </el-menu-item>

          <!-- Configuration -->
          <div class="menu-group-title">Configuration</div>
          <el-menu-item index="/rules/builder">
            <el-icon><Operation /></el-icon>
            <span>Rule Engine</span>
          </el-menu-item>
          <el-sub-menu index="settings">
            <template #title>
              <el-icon><Setting /></el-icon>
              <span>System Settings</span>
            </template>
            <el-menu-item index="/settings/permissions">Permission Matrix</el-menu-item>
            <el-menu-item index="/settings/fields">Field Config</el-menu-item>
          </el-sub-menu>
          
          <!-- Developer Tools -->
          <div class="menu-group-title">Developer</div>
          <el-menu-item index="/developer/card-generator">
            <el-icon><Tools /></el-icon>
            <span>Card Generator</span>
          </el-menu-item>

        </el-menu>
      </div>

      <div class="sidebar-footer">
        <div class="user-profile-mini glass-panel-sm">
          <el-avatar :size="36" src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png" />
          <div class="user-info-mini">
            <div class="user-name">Admin User</div>
            <div class="user-role">System Admin</div>
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
            <div class="search-bar glass-input" @click="focusSearch">
              <el-icon class="search-icon"><Search /></el-icon>
              <input ref="searchInput" type="text" placeholder="Search..." />
              <div class="shortcut-hint">⌘K</div>
            </div>
            
            <el-divider direction="vertical" />
            
            <el-dropdown trigger="click" popper-class="notification-popper">
              <el-badge :value="3" class="notification-badge" type="danger">
                <el-button circle text class="icon-btn">
                  <el-icon><Bell /></el-icon>
                </el-button>
              </el-badge>
              <template #dropdown>
                <div class="notification-dropdown glass-card">
                  <!-- Notification content (same as before) -->
                  <div class="dropdown-header">
                    <span>Notifications</span>
                  </div>
                  <div class="notify-list">
                    <div v-for="i in 3" :key="i" class="notify-item">
                       <div class="notify-icon"><el-icon><InfoFilled /></el-icon></div>
                       <div class="notify-content">
                         <b>Contract Update</b>
                         <p>Status changed...</p>
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
  DataLine, Document, Setting, Bell, Search, InfoFilled, 
  Warning, Connection, Hide, DocumentChecked, Operation, Tools 
} from '@element-plus/icons-vue'
import { ref, onMounted, onUnmounted } from 'vue'

const searchInput = ref(null)

const focusSearch = () => {
  searchInput.value?.focus()
}

const handleKeydown = (e) => {
  if ((e.metaKey || e.ctrlKey) && e.key === 'k') {
    e.preventDefault()
    focusSearch()
  }
}

onMounted(() => {
  window.addEventListener('keydown', handleKeydown)
})

onUnmounted(() => {
  window.removeEventListener('keydown', handleKeydown)
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

.glass-input {
  background: rgba(255, 255, 255, 0.6);
  border: 1px solid rgba(203, 213, 225, 0.6);
  backdrop-filter: blur(4px);
}

.search-bar {
  display: flex;
  align-items: center;
  padding: 8px 16px;
  border-radius: 12px;
  width: 300px;
  transition: all 0.2s;
}

.search-bar:focus-within {
  background: #fff;
  border-color: #3B82F6;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
  transform: scale(1.02);
}

.search-bar input {
  border: none;
  outline: none;
  background: transparent;
  margin-left: 10px;
  flex: 1;
  font-size: 14px;
  color: #1E293B;
}

.shortcut-hint {
  font-size: 11px;
  font-weight: 600;
  color: #94A3B8;
  background: #F1F5F9;
  padding: 4px 6px;
  border-radius: 6px;
  border: 1px solid #E2E8F0;
}

.main-content-wrapper {
  padding: 32px 40px;
}

/* Transitions */
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
