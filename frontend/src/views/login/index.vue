<template>
  <div class="login-container">
    <div class="login-background">
      <div class="shape shape-1"></div>
      <div class="shape shape-2"></div>
    </div>
    
    <div class="login-content">
      <div class="brand-side">
        <div class="logo">
          <div class="logo-icon"></div>
          <h1>{{ $t('common.appTitle') }}</h1>
        </div>
        <p class="brand-subtitle">{{ $t('login.subtitle') }}</p>
        
        <div class="feature-list">
          <div class="feature-item"><el-icon><Check /></el-icon> {{ $t('login.aiAnalysis') }}</div>
          <div class="feature-item"><el-icon><Check /></el-icon> {{ $t('login.riskDetection') }}</div>
          <div class="feature-item"><el-icon><Check /></el-icon> {{ $t('login.smartWorkflows') }}</div>
        </div>
      </div>

      <!-- Login Form Side -->
      <div class="form-side">
        <div class="login-box">
          <h2>{{ $t('login.login') }}</h2>
          <el-form :model="loginForm" label-position="top" @submit.prevent="handleLogin">
            <el-form-item :label="$t('login.username')">
              <el-input v-model="loginForm.username" name="username" placeholder="admin@example.com">
                <template #prefix><el-icon><User /></el-icon></template>
              </el-input>
            </el-form-item>
            <el-form-item :label="$t('login.password')">
              <el-input v-model="loginForm.password" name="password" type="password" :placeholder="$t('common.placeholder')" show-password>
                <template #prefix><el-icon><Lock /></el-icon></template>
              </el-input>
            </el-form-item>
            <el-form-item :label="$t('login.tenant')">
              <el-input v-model="loginForm.tenantId" name="tenantId" placeholder="tenant-1" />
            </el-form-item>
            <el-button type="primary" native-type="submit" class="login-btn" :loading="loading">
              {{ $t('login.login') }}
            </el-button>
          </el-form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { User, Lock, Check } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const router = useRouter()
const loading = ref(false)

const loginForm = reactive({
  username: '',
  password: '',
  tenantId: 'tenant-1'
})

const handleLogin = async () => {
  if (!loginForm.username || !loginForm.password) {
    ElMessage.warning('Please enter username and password')
    return
  }

  loading.value = true
  try {
    const response = await fetch('/api/auth/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(loginForm)
    })

    if (response.ok) {
      const result = await response.json()
      localStorage.setItem('token', result.token)
      localStorage.setItem('tenantId', loginForm.tenantId)
      localStorage.setItem('username', loginForm.username)
      
      ElMessage.success('Login successful')
      router.push('/dashboard')
    } else {
      ElMessage.error('Invalid credentials')
    }
  } catch (error) {
    console.error('Login failed', error)
    ElMessage.error('Network error during login')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f0f2f5;
  overflow: hidden;
  position: relative;
}

.login-background {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  overflow: hidden;
  z-index: 0;
}

.shape {
  position: absolute;
  filter: blur(80px);
  opacity: 0.4;
}

.shape-1 {
  width: 500px;
  height: 500px;
  background: #3b82f6;
  top: -100px;
  right: -100px;
}

.shape-2 {
  width: 400px;
  height: 400px;
  background: #8b5cf6;
  bottom: -50px;
  left: -50px;
}

.login-content {
  width: 1000px;
  height: 600px;
  display: flex;
  background: rgba(255, 255, 255, 0.7);
  backdrop-filter: blur(20px);
  border-radius: 24px;
  box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  z-index: 1;
}

.brand-side {
  flex: 1;
  background: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%);
  padding: 60px;
  color: white;
  display: flex;
  flex-direction: column;
}

.logo {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 40px;
}

.logo-icon {
  width: 48px;
  height: 48px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
}

.logo-icon::after {
  content: 'CM';
  font-weight: 800;
  font-size: 20px;
}

.logo h1 {
  margin: 0;
  font-size: 28px;
  font-weight: 800;
}

.brand-subtitle {
  font-size: 18px;
  opacity: 0.9;
  line-height: 1.6;
  margin-bottom: auto;
}

.feature-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.feature-item {
  display: flex;
  align-items: center;
  gap: 12px;
  font-weight: 500;
}

.form-side {
  width: 450px;
  padding: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: white;
}

.login-box {
  width: 100%;
}

.login-box h2 {
  font-size: 32px;
  font-weight: 800;
  color: #1e293b;
  margin: 0 0 32px 0;
}

.login-btn {
  width: 100%;
  height: 48px;
  margin-top: 24px;
  font-size: 16px;
  font-weight: 600;
  border-radius: 12px;
}

:deep(.el-form-item__label) {
  font-weight: 600;
  color: #64748b;
}

:deep(.el-input__wrapper) {
  border-radius: 12px;
  padding: 8px 16px;
}
</style>
