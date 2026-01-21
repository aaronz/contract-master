export default {
  common: {
    dashboard: '仪表盘',
    appTitle: '合同大师',
    contracts: '合同管理',
    compliance: '合规风控',
    integrations: '系统集成',
    settings: '系统设置',
    search: '搜索',
    save: '保存',
    cancel: '取消',
    delete: '删除',
    edit: '编辑',
    view: '查看',
    hide: '隐藏',
    notifications: '通知中心',
    markAllRead: '全部标记已读',
    noNotifications: '暂无通知',
    userGuide: '用户指南',
    confirm: '确认',
    back: '返回',
    actions: '操作',
    loading: '加载中...',
    success: '成功',
    error: '错误',
    warning: '警告',
    info: '提示',
    filter: '筛选',
    export: '导出',
    create: '创建',
    to: '至',
    time: '时间',
    user: '用户',
    action: '操作',
    resource: '资源',
    status: '状态',
    field: '字段',
    summary: '摘要',
    details: '详情',
    releaseNotes: '发布日志',
    internalField: '内部字段',
    downstreamField: '下游字段',
    transformation: '转换规则',
    enabled: '启用',
    systemName: '系统名称',
    endpointUrl: '接口地址',
    authType: '认证类型',
    label: '标签',
    accessKeyId: 'Access Key ID',
    secretKey: 'Secret Key',
    lastUsed: '最后使用',
    name: '名称',
    targetUrl: '目标地址',
    events: '事件',
    moduleAction: '模块与操作',
    placeholder: '请输入',
    selectPlaceholder: '请选择',
    all: '全部',
    roles: {
      admin: '系统管理员',
      legal_mgr: '法务经理',
      sales_lead: '销售总监',
      sales: '销售代表',
      finance: '财务审计'
    }
  },
  login: {
    title: '合同大师',
    subtitle: '企业级合同合规中间件',
    username: '邮箱或用户名',
    password: '密码',
    tenant: '租户 ID',
    login: '登录',
    aiAnalysis: 'AI 自动分析',
    riskDetection: '风险实时检测',
    smartWorkflows: '智能审批流'
  },
  menu: {
    coreOperations: '核心业务',
    integrationsHub: '集成中心',
    riskCompliance: '风控合规',
    configuration: '系统配置',
    developer: '开发者工具',
    connectors: '连接器',
    hubOverview: '集成概览',
    connectors: '下游系统',
    crmIntegration: 'CRM 集成配置',
    fieldMapping: '字段映射',
    webhooks: 'Webhooks',
    secretsKeys: '密钥管理',
    problemCenter: '合规驾驶舱',
    dataMasking: '数据脱敏',
    auditLogs: '审计日志',
    ruleEngine: '规则引擎',
    evaluationRules: '合规规则库',
    roleManagement: '角色管理',
    aiConfiguration: 'AI模型配置',
    userManagement: '用户管理',
    systemSettings: '系统设置',
    permissionMatrix: '权限矩阵',
    fieldConfig: '字段配置',
    cardGenerator: '卡片生成器'
  },
  dashboard: {
    title: '仪表盘概览',
    subtitle: '欢迎回来，管理员。这是今天的系统状态。',
    totalContracts: '合同总数',
    pendingApprovals: '待审批',
    activeValue: '活跃合同金额',
    riskAlerts: '风险告警',
    volumeTrend: '合同量趋势',
    riskRadar: '风险雷达',
    problemProcessing: '问题处理进度',
    onTrack: '运行正常',
    recentActivity: '最近动态',
    myTasks: '我的任务',
    exportReport: '导出报告',
    last30Days: '最近30天'
  },
  contract: {
    list: '合同列表',
    detail: '合同详情',
    create: '创建合同',
    no: '合同编号',
    name: '合同名称',
    type: '合同类型',
    status: '合同状态',
    amount: '合同金额',
    parties: '合同各方',
    partyA: '甲方 (本方)',
    partyB: '乙方 (对方)',
    reEvaluate: '重新评估',
    generalInfo: '基本信息',
    financials: '财务明细',
    performance: '履约标的',
    legalDates: '法律条款',
    attachments: '附件管理',
    comments: '协作讨论',
    auditLog: '审计日志',
    subtitle: '轻松管理您的合同全生命周期，包括审批、执行与归档。',
    allContracts: '全部合同',
    myPending: '我的待办',
    sharedWithMe: '与我共享',
    searchPlaceholder: '搜索合同名称、编号...',
    itemsSelected: '项已选择',
    advancedFilters: '高级筛选',
    dateRange: '日期范围',
    min: '最小',
    max: '最大',
    applyFilters: '应用筛选',
    assign: '指派',
    archive: '归档',
    date: '日期',
    enums: {
      status: {
        active: '生效中',
        draft: '草稿',
        pending: '待审批',
        expired: '已逾期',
        ai_extracted: 'AI 已提取',
        verified: '已验证',
        published: '已发布'
      },
      currency: {
        cny: '人民币 (CNY)',
        usd: '美元 (USD)',
        eur: '欧元 (EUR)'
      },
      payment: {
        bank: '银行转账',
        check: '支票',
        cash: '现金'
      },
      subject: {
        goods: '货物',
        services: '服务'
      },
      resolution: {
        negotiation: '协商',
        arbitration: '仲裁',
        litigation: '诉讼'
      },
      roleType: {
        standard: '标准',
        system: '系统'
      }
    }
  },
  compliance: {
    problems: '合规驾驶舱',
    audit: '审计日志',
    masking: '数据脱敏',
    rules: '合规规则库',
    severity: {
      severe: '严重',
      warning: '警告',
      info: '提示'
    },
    status: {
      new: '新建',
      acknowledged: '已确认',
      resolved: '已解决',
      ignored: '已忽略'
    },
    ruleId: '规则 ID',
    issueDescription: '问题描述',
    detectedAt: '检测时间',
    updateStatus: '更新状态',
    assignee: '负责人',
    notes: '备注',
    changeDetail: '变更详情',
    oldValue: '原始值',
    newValue: '新值'
  },
  ai: {
    configTitle: 'AI 模型配置',
    configSubtitle: '配置大语言模型 (LLM) 服务商及合同提取提示词。',
    modelConnection: '模型连接',
    extractionLogic: '提取逻辑',
    provider: '服务商',
    modelName: '模型名称',
    apiKey: 'API 密钥',
    endpointUrl: '接口地址',
    systemPrompt: '系统提示词',
    promptTip: '提示：使用 “JSON格式”、“提取合同号” 等关键词来引导 AI。',
    extractionAreaTitle: 'AI 自动提取',
    extractionAreaSubtitle: '上传合同文档，系统将通过 AI 自动识别并填充表单。',
    startAnalysis: '开始分析',
    reExtract: '重新提取',
    chooseFile: '选择文件',
    newVersion: '上传新版本',
    syncFromDoc: '从文档同步 AI 识别结果'
  },
  settings: {
    dataScope: '数据范围',
    roleMatrix: '权限矩阵',
    scopes: {
      all: '全局 (所有数据)',
      dept: '部门级',
      self: '仅限个人'
    }
  },
  guide: {
    subtitle: '全面掌握 AI 驱动的合同合规与管理系统',
    sections: {
      overview: {
        title: '系统概览',
        description: '合同大师（Contract Master）是一个基于 AI 的合规平台，旨在自动完成合同提取、风险评估和全生命周期管理。',
        tip1: '使用仪表盘从高维度查看您的合同组合。',
        tip2: '全局搜索 (Ctrl+K) 帮助您立即找到任何合同或规则。'
      },
      contract: {
        title: '合同管理',
        description: '管理您的合同，从录入到归档。',
        step1: { title: '录入', content: '上传 PDF/Docx 文件或从下游 CRM 系统同步。' },
        step2: { title: 'AI 提取', content: '系统自动使用 OCR 和大模型提取关键字段（各方、日期、金额）。' },
        step3: { title: '验证', content: '在详情页查看 AI 建议值并确认，将状态更改为“已验证”。' }
      },
      rbac: {
        title: '身份与权限管理',
        description: '具备多租户隔离的高级身份与访问控制（RBAC）。',
        step1: { title: '角色定义', content: '在角色管理中定义法务、财务或销售等业务角色。' },
        step2: { title: '用户分配', content: '在您的特定租户下为用户分配一个或多个角色。' },
        step3: { title: '数据范围', content: '配置权限矩阵，限制数据可见性为全局、部门或仅限个人。' }
      },
      compliance: {
        title: '合规与规则',
        description: '使用动态规则引擎进行自动风险检测。',
        step1: { title: '规则定义', content: '在规则管理部分使用 Groovy 脚本或正则创建规则。' },
        step2: { title: '触发机制', content: '规则在保存时自动运行，也可通过“重新评估”手动触发。' },
        step3: { title: '问题解决', content: '检出的问题出现在合规驾驶舱。确认或解决它们以清除告警。' },
        tip1: 'Groovy 规则允许复杂逻辑，如“如果金额 > 100万，乙方必须提供已验证的税号”。',
        tip2: '如果您只想检查特定的合规项，可以选择运行特定规则。'
      },
      dashboard: {
        title: '数据看板与分析',
        description: '实时统计与可视化洞察。',
        tip1: '合同量趋势显示您的业务活跃度随时间的变化。',
        tip2: '风险雷达将威胁归类为法律、财务和操作维度。'
      },
      integrations: {
        title: '系统集成',
        description: '将合同大师连接到您现有的企业技术栈。',
        step1: { title: 'Webhooks', content: '配置端点，在合同验证或发现问题时接收实时通知。' },
        step2: { title: '字段映射', content: '将内部字段映射到外部 CRM 字段，实现无缝同步。' },
        step3: { title: '推送服务', content: '自动将已验证的合同数据分发到 SAP、Salesforce 或自定义 ERP。' }
      },
      ai: {
        title: 'AI 智能化',
        description: '利用大语言模型（LLM）实现合同自动化处理。',
        step1: { title: '模型配置', content: '在设置中配置硅基流动、DeepSeek 或 OpenAI 等服务商。' },
        step2: { title: '自动提取', content: '上传 PDF/Docx 文件，AI 将自动识别并填充合同表单。' },
        step3: { title: '可视化校对', content: '保存前核对以蓝色高亮显示的 AI 提取字段。' }
      }
    }
  },
  releases: {
    title: '发布日志',
    subtitle: '跟踪合同大师的最新功能更新与改进。',
    items: [
      {
        version: 'v1.2.0',
        date: '2026-01-19',
        title: 'AI 智能提取与附件管理上线',
        features: [
          '新增基于 LLM 的 PDF/Docx 合同字段自动提取功能。',
          '集成硅基流动 (SiliconFlow) 作为首选 AI 供应商，支持 Qwen/DeepSeek 模型。',
          '实现 AI 填充字段的视觉高亮提醒，增强人工审核体验。',
          '上线真实的附件管理系统，支持多版本上传、在线预览与下载。',
          '支持管理员自定义 AI 提示词（Prompt）和模型参数。'
        ],
        fixes: [
          '修复了合同附件接口的路由冲突问题。',
          '解决了新创建合同在加载附件时触发的 500 错误。',
          '通过 PDFBox 提升了 PDF 文本提取的准确性。'
        ]
      },
      {
        version: 'v1.1.0',
        date: '2026-01-19',
        title: '稳定性提升与权限体系重构',
        features: [
          '实现了完整的 RBAC 体系，支持动态角色定义和用户分配。',
          '增加了数据库层面的数据范围引擎（全局/部门/个人）。',
          '完成了集成中心建设，支持实时推送和字段转换映射。',
          '全系统支持中英文双语切换（i18n）。',
          '实现了合规风险的系统级实时通知。'
        ],
        fixes: [
          '修复了合规驾驶舱在页面刷新时数据消失的 Bug。',
          '解决了通知中心在多租户环境下的隔离缺陷。',
          '修正了权限矩阵表格在某些分辨率下的布局问题。'
        ]
      },
      {
        version: 'v1.0.5',
        date: '2026-01-15',
        title: '核心引擎增强',
        features: [
          '增强了 Groovy 规则执行器，支持标准字段自动打平。',
          '增加了环境依赖检查脚本 (JDK 17+, Node 18+)。',
          '提升了 AI 在多页长文档提取中的稳定性。'
        ]
      }
    ]
  }
}
