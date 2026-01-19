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
        <p class="brand-slogan">Enterprise Contract Lifecycle Management</p>
        <div class="brand-features">
          <div class="feature-item"><el-icon><Check /></el-icon> AI Analysis</div>
          <div class="feature-item"><el-icon><Check /></el-icon> Risk Detection</div>
          <div class="feature-item"><el-icon><Check /></el-icon> Smart Workflows</div>
        </div>
      </div>
      
      <div class="form-side">
        <div class="form-header">
          <h2>Welcome Back</h2>
          <p>Please enter your details to sign in.</p>
        </div>
        
        <el-form :model="loginForm" label-position="top" size="large">
          <el-form-item label="Email or Username">
            <el-input v-model="loginForm.username" name="username" placeholder="admin@example.com">
              <template #prefix><el-icon><User /></el-icon></template>
            </el-input>
          </el-form-item>
          <el-form-item label="Password">
            <el-input v-model="loginForm.password" name="password" type="password" placeholder="••••••••" show-password>
              <template #prefix><el-icon><Lock /></el-icon></template>
            </el-input>
          </el-form-item>
          <el-form-item label="Tenant ID">
            <el-input v-model="loginForm.tenantId" name="tenantId" placeholder="tenant-1" />
          </el-form-item>
          
          <div class="form-actions">
            <el-checkbox v-model="rememberMe">Remember me</el-checkbox>
            <el-link type="primary" :underline="false">Forgot password?</el-link>
          </div>
          
          <el-button type="primary" class="login-button" @click="handleLogin" :loading="loading">
            Sign In
          </el-button>
          
          <div class="sso-divider">
            <span>Or continue with</span>
          </div>
          
          <div class="sso-buttons">
            <el-button class="sso-btn">
              <img src="https://www.svgrepo.com/show/475656/google-color.svg" alt="Google" width="20" />
              Google
            </el-button>
            <el-button class="sso-btn">
              <img src="https://www.svgrepo.com/show/448234/microsoft.svg" alt="Microsoft" width="20" />
              Microsoft
            </el-button>
          </div>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { User, Lock, Check } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

const router = useRouter()
const loading = ref(false)
const rememberMe = ref(false)
const loginForm = ref({
  username: '',
  password: '',
  tenantId: 'tenant-1'
})

const handleLogin = async () => {
  loading.value = true
  try {
    const response = await request.post('/auth/login', loginForm.value)
    
    // Check if login was successful
    if (response.data && response.data.token) {
      const data = response.data
      localStorage.setItem('token', data.token)
      localStorage.setItem('tenantId', loginForm.value.tenantId)
      router.push('/')
    } else if (response.data.status === 200 && response.data.data) {
       // Handle wrapped response if auth controller follows ApiResponse
       localStorage.setItem('token', response.data.data.token)
       localStorage.setItem('tenantId', loginForm.value.tenantId)
       router.push('/')
    } else {
      ElMessage.error('Invalid credentials')
    }
  } catch (error) {
    console.error('Login error', error)
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
  background-color: #0F172A;
  position: relative;
  overflow: hidden;
}

.login-background {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 0;
}

.shape {
  position: absolute;
  filter: blur(80px);
  border-radius: 50%;
  opacity: 0.4;
}

.shape-1 {
  width: 500px;
  height: 500px;
  background: #3B82F6;
  top: -100px;
  left: -100px;
  animation: float 10s ease-in-out infinite;
}

.shape-2 {
  width: 400px;
  height: 400px;
  background: #8B5CF6;
  bottom: -50px;
  right: -50px;
  animation: float 12s ease-in-out infinite reverse;
}

@keyframes float {
  0% { transform: translate(0, 0); }
  50% { transform: translate(30px, 30px); }
  100% { transform: translate(0, 0); }
}

.login-content {
  position: relative;
  z-index: 1;
  display: flex;
  width: 900px;
  height: 600px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 24px;
  box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.25);
  overflow: hidden;
}

.brand-side {
  flex: 1;
  background: linear-gradient(135deg, #1E293B, #0F172A);
  padding: 40px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  color: white;
  position: relative;
}

.brand-side::after {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: url('data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMjAiIGhlaWdodD0iMjAiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+PGNpcmNsZSBjeD0iMiIgY3k9IjIiIHI9IjEiIGZpbGw9InJnYmEoMjU1LDI1NSwyNTUsMC4wNSkiLz48L3N2Zz4=');
  opacity: 0.3;
}

.logo {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 24px;
  position: relative;
  z-index: 1;
}

.logo-icon {
  width: 40px;
  height: 40px;
  background: linear-gradient(135deg, #3B82F6, #2563EB);
  border-radius: 10px;
}

.logo h1 {
  font-size: 24px;
  font-weight: 700;
  margin: 0;
}

.brand-slogan {
  font-size: 20px;
  color: #94A3B8;
  margin-bottom: 40px;
  position: relative;
  z-index: 1;
}

.brand-features {
  display: flex;
  flex-direction: column;
  gap: 16px;
  position: relative;
  z-index: 1;
}

.feature-item {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 16px;
  color: #E2E8F0;
}

.feature-item .el-icon {
  color: #10B981;
}

.form-side {
  flex: 1.2;
  padding: 60px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.form-header {
  margin-bottom: 32px;
}

.form-header h2 {
  font-size: 28px;
  font-weight: 700;
  color: #1E293B;
  margin: 0 0 8px 0;
}

.form-header p {
  color: #64748B;
  margin: 0;
}

.form-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.login-button {
  width: 100%;
  font-weight: 600;
  height: 48px;
  border-radius: 12px;
  font-size: 16px;
  background: #2563EB;
}

.sso-divider {
  margin: 24px 0;
  text-align: center;
  position: relative;
}

.sso-divider span {
  background: white;
  padding: 0 12px;
  color: #94A3B8;
  font-size: 14px;
  position: relative;
  z-index: 1;
}

.sso-divider::after {
  content: '';
  position: absolute;
  top: 50%;
  left: 0;
  width: 100%;
  height: 1px;
  background: #E2E8F0;
  z-index: 0;
}

.sso-buttons {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}

.sso-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  height: 44px;
}
</style>
