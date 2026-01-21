export default {
  common: {
    dashboard: 'Dashboard',
    appTitle: 'Contract Master',
    contracts: 'Contracts',
    compliance: 'Compliance',
    integrations: 'Integrations',
    settings: 'Settings',
    search: 'Search',
    save: 'Save',
    cancel: 'Cancel',
    delete: 'Delete',
    edit: 'Edit',
    view: 'View',
    hide: 'Hide',
    notifications: 'Notifications',
    markAllRead: 'Mark all read',
    noNotifications: 'No notifications',
    userGuide: 'User Guide',
    confirm: 'Confirm',
    back: 'Back',
    actions: 'Actions',
    loading: 'Loading...',
    success: 'Success',
    error: 'Error',
    warning: 'Warning',
    info: 'Info',
    filter: 'Filter',
    export: 'Export',
    create: 'Create',
    to: 'To',
    time: 'Time',
    user: 'User',
    action: 'Action',
    resource: 'Resource',
    status: 'Status',
    field: 'Field',
    summary: 'Summary',
    details: 'Details',
    releaseNotes: 'Release Notes',
    internalField: 'Internal Field',
    downstreamField: 'Downstream Field',
    transformation: 'Transformation',
    enabled: 'Enabled',
    systemName: 'System Name',
    endpointUrl: 'Endpoint URL',
    authType: 'Auth Type',
    label: 'Label',
    accessKeyId: 'Access Key ID',
    secretKey: 'Secret Key',
    lastUsed: 'Last Used',
    name: 'Name',
    targetUrl: 'Target URL',
    events: 'Events',
    moduleAction: 'Module & Action',
    placeholder: 'Please enter',
    selectPlaceholder: 'Please select',
    all: 'All',
    records: 'Records',
    duration: 'Duration',
    roles: {
      admin: 'System Admin',
      legal_mgr: 'Legal Manager',
      sales_lead: 'Sales Director',
      sales: 'Sales Rep',
      finance: 'Finance Audit'
    }
  },
  login: {
    title: 'Contract Master',
    subtitle: 'Enterprise Contract Compliance Middleware',
    username: 'Email or Username',
    password: 'Password',
    tenant: 'Tenant ID',
    login: 'Login',
    aiAnalysis: 'AI Analysis',
    riskDetection: 'Risk Detection',
    smartWorkflows: 'Smart Workflows'
  },
  menu: {
    coreOperations: 'Core Operations',
    integrationsHub: 'Integrations Hub',
    riskCompliance: 'Risk & Compliance',
    configuration: 'Configuration',
    developer: 'Developer',
    connectors: 'Connectors',
    hubOverview: 'Hub Overview',
    connectors: 'Downstream Systems',
    crmIntegration: 'CRM Settings',
    fieldMapping: 'Field Mapping',
    webhooks: 'Webhooks',
    secretsKeys: 'Secrets & Keys',
    problemCenter: 'Problem Center',
    dataMasking: 'Data Masking',
    auditLogs: 'Audit Logs',
    ruleEngine: 'Rule Engine',
    evaluationRules: 'Evaluation Rules',
    roleManagement: 'Role Management',
    aiConfiguration: 'AI Configuration',
    userManagement: 'User Management',
    systemSettings: 'System Settings',
    permissionMatrix: 'Permission Matrix',
    fieldConfig: 'Field Config',
    cardGenerator: 'Card Generator'
  },
  dashboard: {
    title: 'Dashboard Overview',
    subtitle: 'Welcome back, Admin. Here\'s what\'s happening today.',
    totalContracts: 'Total Contracts',
    pendingApprovals: 'Pending Approvals',
    activeValue: 'Active Value',
    riskAlerts: 'Risk Alerts',
    volumeTrend: 'Volume Trend',
    riskRadar: 'Risk Radar',
    problemProcessing: 'Problem Processing',
    onTrack: 'On Track',
    recentActivity: 'Recent Activity',
    myTasks: 'My Tasks',
    exportReport: 'Export Report',
    last30Days: 'Last 30 Days'
  },
  contract: {
    list: 'Contract List',
    detail: 'Contract Detail',
    create: 'Create Contract',
    no: 'Contract No',
    name: 'Contract Name',
    type: 'Type',
    status: 'Status',
    amount: 'Amount',
    parties: 'Parties',
    partyA: 'Party A',
    partyB: 'Party B',
    reEvaluate: 'Re-evaluate',
    generalInfo: 'General Info',
    financials: 'Financials',
    performance: 'Performance & Subject',
    legalDates: 'Legal & Dates',
    attachments: 'Attachments',
    comments: 'Comments',
    auditLog: 'Audit Log',
    subtitle: 'Manage all your contracts, approvals, and archives.',
    allContracts: 'All Contracts',
    myPending: 'My Pending',
    sharedWithMe: 'Shared with me',
    searchPlaceholder: 'Search contracts...',
    itemsSelected: 'items selected',
    advancedFilters: 'Advanced Filters',
    dateRange: 'Date Range',
    min: 'Min',
    max: 'Max',
    applyFilters: 'Apply Filters',
    assign: 'Assign',
    archive: 'Archive',
    date: 'Date',
    enums: {
      status: {
        active: 'Active',
        draft: 'Draft',
        pending: 'Pending',
        expired: 'Expired',
        ai_extracted: 'AI Extracted',
        verified: 'Verified',
        published: 'Published'
      },
      currency: {
        cny: 'CNY',
        usd: 'USD',
        eur: 'EUR'
      },
      payment: {
        bank: 'Bank Transfer',
        check: 'Check',
        cash: 'Cash'
      },
      subject: {
        goods: 'Goods',
        services: 'Services'
      },
      resolution: {
        negotiation: 'Negotiation',
        arbitration: 'Arbitration',
        litigation: 'Litigation'
      },
      roleType: {
        standard: 'Standard',
        system: 'System'
      }
    }
  },
  compliance: {
    problems: 'Problem Cockpit',
    audit: 'Audit Logs',
    masking: 'Data Masking',
    rules: 'Compliance Rules',
    severity: {
      severe: 'Severe',
      warning: 'Warning',
      info: 'Info'
    },
    status: {
      new: 'New',
      acknowledged: 'Acknowledged',
      resolved: 'Resolved',
      ignored: 'Ignored'
    },
    ruleId: 'Rule ID',
    issueDescription: 'Issue Description',
    detectedAt: 'Detected At',
    updateStatus: 'Update Status',
    assignee: 'Assignee',
    notes: 'Notes',
    changeDetail: 'Change Detail',
    oldValue: 'Old Value',
    newValue: 'New Value',
    severityShort: '!',
    actions: 'Actions'
  },
  ai: {
    configTitle: 'AI Configuration',
    configSubtitle: 'Configure LLM providers and prompts for automated contract extraction.',
    modelConnection: 'Model Connection',
    extractionLogic: 'Extraction Logic',
    provider: 'Provider',
    modelName: 'Model Name',
    apiKey: 'API Key',
    endpointUrl: 'Endpoint URL',
    systemPrompt: 'System Prompt',
    promptTip: 'Tip: Use keywords like "Extract JSON", "Include contractNo", etc.',
    extractionAreaTitle: 'AI Auto-Extraction',
    extractionAreaSubtitle: 'Upload a contract document to automatically fill out the form using AI.',
    startAnalysis: 'Start Analysis',
    reExtract: 'Re-extract',
    chooseFile: 'Choose File',
    newVersion: 'New Version',
    syncFromDoc: 'AI Sync from Document'
  },
  settings: {
    dataScope: 'Data Scope',
    roleMatrix: 'Permission Matrix',
    scopes: {
      all: 'Global (All Data)',
      dept: 'Department Only',
      self: 'My Records Only'
    }
  },
  guide: {
    subtitle: 'Master the Contract Compliance & Management System',
    sections: {
      overview: {
        title: 'System Overview',
        description: 'Contract Master is an AI-powered compliance platform designed to automate contract extraction, risk evaluation, and lifecycle management.',
        tip1: 'Use the Dashboard for a high-level view of your contract portfolio.',
        tip2: 'Global search (Ctrl+K) helps you find any contract or rule instantly.'
      },
      contract: {
        title: 'Contract Management',
        description: 'Manage your contracts from ingestion to archive.',
        step1: { title: 'Ingestion', content: 'Upload PDF/Docx files or sync from downstream CRM systems.' },
        step2: { title: 'AI Extraction', content: 'The system automatically extracts key fields (Parties, Dates, Amounts) using OCR and LLMs.' },
        step3: { title: 'Verification', content: 'Review AI-suggested values in the Detail page and confirm them to change status to VERIFIED.' }
      },
      rbac: {
        title: 'IAM & RBAC',
        description: 'Advanced Identity and Access Management with multi-tenant isolation.',
        step1: { title: 'Role Definition', content: 'Define business roles like Legal, Finance, or Sales in Role Management.' },
        step2: { title: 'User Assignment', content: 'Assign one or more roles to users within your specific tenant.' },
        step3: { title: 'Data Scoping', content: 'Configure the Permission Matrix to restrict data visibility to Global, Department, or Self-only.' }
      },
      compliance: {
        title: 'Compliance & Rules',
        description: 'Automated risk detection using dynamic rule engines.',
        step1: { title: 'Rule Definition', content: 'Create rules using Groovy scripts or Regex in the Rule Management section.' },
        step2: { title: 'Triggering', content: 'Rules run automatically on save, or can be triggered manually via "Re-evaluate".' },
        step3: { title: 'Resolution', content: 'Found issues appear in the Problem Cockpit. Acknowledge or resolve them to clear the alert.' },
        tip1: 'Groovy rules allow complex logic like "If amount > 1M, Party B must have a verified VAT ID".',
        tip2: 'You can select specific rules to run if you only want to check a particular compliance aspect.'
      },
      dashboard: {
        title: 'Dashboard & Analytics',
        description: 'Real-time statistics and visual insights.',
        tip1: 'The Volume Trend shows your activity over time.',
        tip2: 'Risk Radar categorizes threats into Legal, Financial, and Operational dimensions.'
      },
      integrations: {
        title: 'Integrations',
        description: 'Connect Contract Master to your existing enterprise stack.',
        step1: { title: 'Webhooks', content: 'Configure endpoints to receive notifications when contracts are verified or issues are found.' },
        step2: { title: 'Field Mapping', content: 'Map internal fields to external CRM fields for seamless synchronization.' },
        step3: { title: 'Push Service', content: 'Automatically distribute verified contract data to SAP, Salesforce, or custom ERPs.' }
      },
      ai: {
        title: 'AI Intelligence',
        description: 'Leverage LLMs for automated contract processing.',
        step1: { title: 'LLM Configuration', content: 'Set up providers like SiliconFlow, DeepSeek, or OpenAI in Settings.' },
        step2: { title: 'Auto-Extraction', content: 'Upload PDF/Docx files to automatically fill out contract forms.' },
        step3: { title: 'Visual Review', content: 'Verify AI-extracted fields highlighted in blue before saving.' }
      }
    }
  },
  releases: {
    title: 'Release Notes',
    subtitle: 'Track latest updates and improvements to Contract Master.',
    items: [
      {
        version: 'v1.2.0',
        date: '2026-01-19',
        title: 'AI Intelligence & Document Management',
        features: [
          'Added AI Auto-Extraction for PDF/Docx documents with LLM support.',
          'Integrated SiliconFlow as a primary AI provider (DeepSeek/Qwen models).',
          'Implemented visual field highlighting for AI-suggested values.',
          'Added real Attachment Management system (upload, preview, download).',
          'Configurable LLM prompts and model settings for administrators.'
        ],
        fixes: [
          'Fixed API routing collision for contract attachment endpoints.',
          'Resolved 500 error when fetching attachments for newly created contracts.',
          'Improved PDF text extraction reliability via PDFBox.'
        ]
      },
      {
        version: 'v1.1.0',
        date: '2026-01-19',
        title: 'Stabilization & IAM Overhaul',
        features: [
          'Implemented full RBAC with dynamic role definition and user assignment.',
          'Added Data Scoping engine (Global/Dept/Self) at the database level.',
          'Completed Integration Hub with real-time push and field mapping.',
          'Full internationalization (i18n) support for English and Chinese.',
          'Live system notifications for compliance alerts.'
        ],
        fixes: [
          'Fixed data disappearing bug in Compliance Cockpit on page refresh.',
          'Resolved multi-tenancy isolation gaps in Notification center.',
          'Corrected UI layout issues in Permission Matrix table.'
        ]
      },
      {
        version: 'v1.0.5',
        date: '2026-01-15',
        title: 'Core Engine Enhancements',
        features: [
          'Enhanced Groovy rule executor with standard field flattening.',
          'Added environment dependency check scripts (JDK 17+, Node 18+).',
          'Improved AI extraction reliability for multi-page documents.'
        ]
      }
    ]
  }
}
