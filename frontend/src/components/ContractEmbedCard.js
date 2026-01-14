class ContractCard extends HTMLElement {
  constructor() {
    super();
    this.attachShadow({ mode: 'open' });
  }

  connectedCallback() {
    const contractId = this.getAttribute('contract-id');
    this.render(contractId);
  }

  render(id) {
    this.shadowRoot.innerHTML = `
      <style>
        .card {
          font-family: system-ui, -apple-system, sans-serif;
          border: 1px solid #e4e7ed;
          border-radius: 8px;
          padding: 16px;
          background: #f8fafc;
          box-shadow: 0 4px 6px -1px rgb(0 0 0 / 0.1);
          max-width: 300px;
        }
        .header { font-weight: bold; color: #1e293b; margin-bottom: 8px; border-bottom: 1px solid #ddd; padding-bottom: 4px; }
        .item { font-size: 14px; margin: 4px 0; color: #475569; }
        .tag { background: #dcfce7; color: #166534; padding: 2px 6px; border-radius: 4px; font-size: 12px; }
      </style>
      <div class="card">
        <div class="header">Contract Element Hub</div>
        <div class="item"><b>ID:</b> ${id}</div>
        <div class="item"><b>Status:</b> <span class="tag">SYNCED</span></div>
        <div class="item"><b>Risk:</b> <span style="color: #ef4444">Low</span></div>
        <button style="margin-top: 8px; width: 100%; cursor: pointer;">View in Hub</button>
      </div>
    `;
  }
}

customElements.define('contract-hub-card', ContractCard);
