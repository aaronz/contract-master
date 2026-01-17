<template>
  <div class="dashboard-container">
    <!-- Header Section -->
    <div class="dashboard-header">
      <div>
        <h1 class="page-title">Dashboard Overview</h1>
        <p class="page-subtitle">Welcome back, Admin. Here's what's happening today.</p>
      </div>
      <div class="header-actions">
        <el-button>Last 30 Days <el-icon class="el-icon--right"><ArrowDown /></el-icon></el-button>
        <el-button type="primary" :icon="Download" @click="handleExport">Export Report</el-button>
      </div>
    </div>

    <!-- Stats Cards -->
    <div class="stats-grid">
      <div class="stat-card glass-card hover-scale">
        <div class="stat-icon bg-blue-100 text-blue-600">
          <el-icon><Document /></el-icon>
        </div>
        <div class="stat-content">
          <span class="stat-label">Total Contracts</span>
          <div class="stat-value">{{ stats.totalContracts }}</div>
          <div class="stat-trend positive">
            <el-icon><Top /></el-icon> 12% from last month
          </div>
        </div>
      </div>

      <div class="stat-card glass-card hover-scale">
        <div class="stat-icon bg-orange-100 text-orange-600">
          <el-icon><Timer /></el-icon>
        </div>
        <div class="stat-content">
          <span class="stat-label">Pending Approval</span>
          <div class="stat-value">{{ stats.pendingApprovals }}</div>
          <div class="stat-trend negative">
            <el-icon><Top /></el-icon> 4 new today
          </div>
        </div>
      </div>

      <div class="stat-card glass-card hover-scale">
        <div class="stat-icon bg-green-100 text-green-600">
          <el-icon><Money /></el-icon>
        </div>
        <div class="stat-content">
          <span class="stat-label">Active Value</span>
          <div class="stat-value">${{ stats.activeValue }}</div>
          <div class="stat-trend positive">
            <el-icon><Top /></el-icon> 8.5% YoY
          </div>
        </div>
      </div>

      <div class="stat-card glass-card hover-scale">
        <div class="stat-icon bg-red-100 text-red-600">
          <el-icon><Warning /></el-icon>
        </div>
        <div class="stat-content">
          <span class="stat-label">Risk Alerts</span>
          <div class="stat-value">{{ stats.riskAlerts }}</div>
          <div class="stat-trend negative">
            <el-icon><Top /></el-icon> 2 critical
          </div>
        </div>
      </div>
    </div>

    <!-- Charts Section -->
    <div class="charts-grid">
      <div class="chart-card glass-card main-chart">
        <div class="card-header">
          <span>Contract Volume Trend</span>
          <el-radio-group v-model="chartPeriod" size="small">
            <el-radio-button value="Week">Week</el-radio-button>
            <el-radio-button value="Month">Month</el-radio-button>
            <el-radio-button value="Year">Year</el-radio-button>
          </el-radio-group>
        </div>
        <div ref="trendChartRef" class="chart-container"></div>
      </div>

      <div class="chart-card glass-card">
        <div class="card-header">
          <span>Risk Analysis Radar</span>
          <el-button link type="primary">View Details</el-button>
        </div>
        <div ref="radarChartRef" class="chart-container"></div>
      </div>

      <div class="chart-card glass-card">
        <div class="card-header">
          <span>Problem Processing</span>
          <el-tag type="success" size="small">On Track</el-tag>
        </div>
        <div ref="gaugeChartRef" class="chart-container"></div>
      </div>
    </div>

    <!-- Recent Activity & Tasks -->
    <div class="bottom-grid">
      <div class="list-card glass-card">
        <div class="card-header">
          <span>Recent Activity</span>
        </div>
        <div class="activity-feed">
          <div v-for="(activity, index) in recentActivities" :key="index" class="activity-item">
            <div class="activity-timeline">
              <div class="dot" :class="activity.type"></div>
              <div class="line" v-if="index !== recentActivities.length - 1"></div>
            </div>
            <div class="activity-details">
              <div class="activity-header">
                <span class="user">{{ activity.user }}</span>
                <span class="action">{{ activity.action }}</span>
                <span class="target">{{ activity.target }}</span>
              </div>
              <span class="time">{{ activity.time }}</span>
            </div>
          </div>
        </div>
      </div>

      <div class="list-card glass-card">
        <div class="card-header">
          <span>My Tasks</span>
          <el-button type="primary" link>View All</el-button>
        </div>
        <div class="task-list">
          <div v-for="task in myTasks" :key="task.id" class="task-item">
            <el-checkbox v-model="task.completed">
              <span :class="{ completed: task.completed }">{{ task.content }}</span>
            </el-checkbox>
            <el-tag size="small" :type="task.priority === 'High' ? 'danger' : 'warning'">{{ task.priority }}</el-tag>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { Document, Timer, Money, Warning, Top, Download, ArrowDown } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import * as echarts from 'echarts'

const router = useRouter()
const chartPeriod = ref('Month')
const trendChartRef = ref(null)
const radarChartRef = ref(null)
const gaugeChartRef = ref(null)
let trendChart = null
let radarChart = null
let gaugeChart = null

const recentActivities = ref([])

const myTasks = ref([])

const initCharts = () => {
  // Trend Chart
  trendChart = echarts.init(trendChartRef.value)
  trendChart.on('click', (params) => {
    router.push({ path: '/contracts', query: { date: params.name } })
  })
  trendChart.setOption({
    tooltip: { trigger: 'axis' },
    grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
      axisLine: { show: false },
      axisTick: { show: false }
    },
    yAxis: { type: 'value', splitLine: { lineStyle: { type: 'dashed' } } },
    series: [
      {
        name: 'Contracts',
        type: 'line',
        smooth: true,
        data: [120, 132, 101, 134, 90, 230, 210],
        itemStyle: { color: '#3B82F6' },
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(59, 130, 246, 0.3)' },
            { offset: 1, color: 'rgba(59, 130, 246, 0)' }
          ])
        }
      }
    ]
  })

  // Radar Chart
  radarChart = echarts.init(radarChartRef.value)
  radarChart.setOption({
    radar: {
      indicator: [
        { name: 'Financial', max: 100 },
        { name: 'Legal', max: 100 },
        { name: 'Compliance', max: 100 },
        { name: 'Operational', max: 100 },
        { name: 'Performance', max: 100 }
      ],
      radius: '65%',
      splitArea: {
        areaStyle: {
          color: ['rgba(255,255,255,0)', 'rgba(255,255,255,0)']
        }
      }
    },
    series: [
      {
        name: 'Risk Assessment',
        type: 'radar',
        data: [
          {
            value: [40, 20, 60, 30, 50],
            name: 'Current Portfolio',
            itemStyle: { color: '#EF4444' },
            areaStyle: { color: 'rgba(239, 68, 68, 0.2)' }
          }
        ]
      }
    ]
  })

  // Gauge Chart
  gaugeChart = echarts.init(gaugeChartRef.value)
  gaugeChart.setOption({
    series: [
      {
        type: 'gauge',
        startAngle: 180,
        endAngle: 0,
        min: 0,
        max: 100,
        splitNumber: 8,
        itemStyle: {
          color: '#10B981'
        },
        progress: {
          show: true,
          width: 30
        },
        pointer: {
          show: false
        },
        axisLine: {
          lineStyle: {
            width: 30
          }
        },
        axisTick: {
          show: false
        },
        splitLine: {
          show: false
        },
        axisLabel: {
          show: false
        },
        detail: {
          valueAnimation: true,
          offsetCenter: [0, '20%'],
          fontSize: 40,
          fontWeight: 'bold',
          formatter: '{value}%',
          color: '#1E293B'
        },
        data: [
          {
            value: 85,
            name: 'Resolved'
          }
        ]
      }
    ]
  })
}

const stats = ref({
  totalContracts: 0,
  pendingApprovals: 0,
  activeValue: 0,
  riskAlerts: 0
})

const fetchDashboardStats = async () => {
  try {
    const response = await fetch('/api/dashboard/stats', {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
        'X-Tenant-ID': localStorage.getItem('tenantId')
      }
    })
    if (response.ok) {
      const result = await response.json()
      stats.value = result.data
      updateVisuals(result.data)
    }
  } catch (error) {
    console.error('Failed to fetch dashboard stats', error)
    ElMessage.error('Failed to load dashboard statistics')
  }
}

const updateVisuals = (data) => {
  if (trendChart && data.volumeTrend) {
    trendChart.setOption({
      xAxis: { data: data.volumeTrend.map(d => d.date) },
      series: [{ data: data.volumeTrend.map(d => d.count) }]
    })
  }
  if (radarChart && data.riskRadar) {
    radarChart.setOption({
      series: [{
        data: [{
          value: data.riskRadar.map(r => r.value),
          name: 'Current Portfolio'
        }]
      }]
    })
  }
  if (data.recentActivities) {
    recentActivities.value = data.recentActivities
  }
  if (data.myTasks) {
    myTasks.value = data.myTasks
  }
}

onMounted(() => {
  initCharts()
  fetchDashboardStats()
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  if (trendChart) trendChart.dispose()
  if (radarChart) radarChart.dispose()
  if (gaugeChart) gaugeChart.dispose()
})

const handleResize = () => {
  trendChart && trendChart.resize()
  radarChart && radarChart.resize()
  gaugeChart && gaugeChart.resize()
}
const handleExport = async () => {
  try {
    const response = await fetch('/api/export/contracts', {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
        'X-Tenant-ID': localStorage.getItem('tenantId')
      }
    })
    const blob = await response.blob()
    const url = window.URL.createObjectURL(blob)
    const a = document.createElement('a')
    a.href = url
    a.download = 'contracts.csv'
    document.body.appendChild(a)
    a.click()
    window.URL.revokeObjectURL(url)
  } catch (error) {
    console.error('Export failed', error)
    ElMessage.error('Failed to export report')
  }
}
</script>

<style scoped>
.dashboard-container {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.dashboard-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
}

.page-title {
  font-size: 24px;
  font-weight: 700;
  color: var(--text-primary);
  margin: 0 0 8px 0;
}

.page-subtitle {
  color: var(--text-secondary);
  margin: 0;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 24px;
}

.stat-card {
  padding: 24px;
  display: flex;
  align-items: flex-start;
  transition: transform 0.2s, box-shadow 0.2s;
}

.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 10px 20px rgba(0,0,0,0.1);
}

.chart-card {
  padding: 24px;
  border-radius: 16px;
}

.list-card {
  padding: 24px;
  border-radius: 16px;
}

.stat-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  margin-right: 16px;
}

.bg-blue-100 { background: #EFF6FF; }
.text-blue-600 { color: #2563EB; }
.bg-orange-100 { background: #FFF7ED; }
.text-orange-600 { color: #EA580C; }
.bg-green-100 { background: #F0FDF4; }
.text-green-600 { color: #16A34A; }
.bg-red-100 { background: #FEF2F2; }
.text-red-600 { color: #DC2626; }

.stat-content {
  flex: 1;
}

.stat-label {
  display: block;
  color: var(--text-secondary);
  font-size: 14px;
  margin-bottom: 4px;
}

.stat-value {
  font-size: 28px;
  font-weight: 700;
  color: var(--text-primary);
  margin-bottom: 8px;
  line-height: 1.2;
}

.stat-trend {
  font-size: 13px;
  display: flex;
  align-items: center;
  gap: 4px;
}

.stat-trend.positive { color: var(--success-color); }
.stat-trend.negative { color: var(--danger-color); }

.charts-grid {
  display: grid;
  grid-template-columns: 2fr 1fr 1fr;
  gap: 24px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 600;
}

.chart-container {
  height: 300px;
  width: 100%;
}

.bottom-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 24px;
}

.activity-item {
  display: flex;
  gap: 16px;
  padding-bottom: 20px;
}

.activity-timeline {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.dot {
  width: 10px;
  height: 10px;
  border-radius: 50%;
  border: 2px solid white;
  box-shadow: 0 0 0 2px #E2E8F0;
}
.dot.success { background-color: var(--success-color); box-shadow: 0 0 0 2px var(--success-color); }
.dot.info { background-color: var(--info-color); box-shadow: 0 0 0 2px var(--info-color); }
.dot.danger { background-color: var(--danger-color); box-shadow: 0 0 0 2px var(--danger-color); }
.dot.warning { background-color: var(--warning-color); box-shadow: 0 0 0 2px var(--warning-color); }

.line {
  flex: 1;
  width: 2px;
  background-color: #F1F5F9;
  margin-top: 4px;
}

.activity-header {
  font-size: 14px;
  margin-bottom: 4px;
}

.user { font-weight: 600; color: var(--text-primary); }
.action { color: var(--text-secondary); margin: 0 4px; }
.target { font-weight: 500; color: var(--accent-color); }
.time { font-size: 12px; color: #94A3B8; }

.task-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.task-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px;
  background: #F8FAFC;
  border-radius: 8px;
  transition: all 0.2s;
}

.task-item:hover {
  background: #EFF6FF;
}

.completed {
  text-decoration: line-through;
  color: #94A3B8;
}
</style>
