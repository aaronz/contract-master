import { createRouter, createWebHistory } from 'vue-router'
import Layout from '@/layout/index.vue'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue'),
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/index.vue'),
      },
      // Contract Management
      {
        path: 'contracts',
        name: 'Contracts',
        component: () => import('@/views/contract/list.vue'),
      },
      {
        path: 'contracts/:id',
        name: 'ContractDetail',
        component: () => import('@/views/contract/detail.vue'),
      },
      
      // Integrations Hub
      {
        path: 'integrations',
        name: 'IntegrationsHub',
        component: () => import('@/views/integrations/index.vue'),
      },
      {
        path: 'integrations/webhooks',
        name: 'Webhooks',
        component: () => import('@/views/integrations/webhooks.vue'),
      },
      {
        path: 'integrations/secrets',
        name: 'SecretsManagement',
        component: () => import('@/views/integrations/secrets.vue'),
      },
      {
        path: 'integrations/mapping',
        name: 'FieldMapping',
        component: () => import('@/views/integrations/mapping.vue'),
      },

      // Compliance & Risk
      {
        path: 'compliance/problems',
        name: 'ProblemCenter',
        component: () => import('@/views/problem/ProblemCockpit.vue'),
      },
      {
        path: 'compliance/masking',
        name: 'DataMasking',
        component: () => import('@/views/compliance/masking.vue'),
      },
      {
        path: 'compliance/audit',
        name: 'ComplianceAudit',
        component: () => import('@/views/compliance/audit.vue'),
      },

      // Configuration & Tools
      {
        path: 'rules/list',
        name: 'RuleList',
        component: () => import('@/views/rule/RuleList.vue'),
      },
      {
        path: 'settings/permissions',
        name: 'PermissionMatrix',
        component: () => import('@/views/settings/role-matrix.vue'), // Keep existing file for now, might refactor
      },
      {
        path: 'settings/fields',
        name: 'FieldConfig',
        component: () => import('@/views/settings/fields.vue'),
      },
      {
        path: 'developer/card-generator',
        name: 'CardGenerator',
        component: () => import('@/views/developer/card-generator.vue'),
      },
      {
        path: 'guide',
        name: 'UserGuide',
        component: () => import('@/views/guide/index.vue'),
      },
    ],
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.path !== '/login' && !token) {
    next('/login')
  } else {
    next()
  }
})

export default router