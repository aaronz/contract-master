<template>
  <div 
    class="pdf-viewer-container glass-card" 
    v-loading="loading"
    element-loading-text="Loading Document..."
    element-loading-background="rgba(255, 255, 255, 0.7)"
  >
    <div class="pdf-header glass-panel">
      <div class="pdf-title">
        <span v-if="fileName">{{ fileName }}</span>
        <span v-else>Document Preview</span>
      </div>
      <div class="pdf-controls">
        <span class="page-count" v-if="numPages > 0">
          {{ numPages }} Pages
        </span>
      </div>
    </div>

    <div class="pdf-scroll-area custom-scrollbar" ref="scrollContainer">
      <div v-if="error" class="error-state">
        <el-icon class="error-icon"><Warning /></el-icon>
        <p>{{ error }}</p>
      </div>

      <div 
        v-else
        v-for="pageNum in numPages" 
        :key="pageNum" 
        class="pdf-page-wrapper" 
        :id="`pdf-page-${pageNum}`"
      >
        <div class="pdf-page-content" :style="{ width: getPageWidth(pageNum) + 'px', height: getPageHeight(pageNum) + 'px' }">
          <canvas :ref="el => setCanvasRef(el, pageNum)" class="pdf-canvas"></canvas>
          
          <!-- Highlights Overlay -->
          <svg 
            class="highlight-overlay" 
            :viewBox="`0 0 ${getPageViewport(pageNum)?.width || 0} ${getPageViewport(pageNum)?.height || 0}`"
          >
            <rect 
              v-for="(rect, idx) in getHighlightsForPage(pageNum)" 
              :key="idx"
              :x="rect.x"
              :y="rect.y"
              :width="rect.width"
              :height="rect.height"
              class="highlight-rect"
            />
          </svg>
        </div>
        <div class="page-number">Page {{ pageNum }}</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, shallowRef, computed, nextTick } from 'vue';
import * as pdfjsLib from 'pdfjs-dist';
import { Warning } from '@element-plus/icons-vue';

// Import worker
import pdfWorker from 'pdfjs-dist/build/pdf.worker.mjs?url';
pdfjsLib.GlobalWorkerOptions.workerSrc = pdfWorker;

const props = defineProps({
  url: {
    type: String,
    required: true
  },
  highlights: {
    type: Array,
    default: () => []
  },
  fileName: {
    type: String,
    default: ''
  }
});

const loading = ref(true);
const error = ref(null);
const pdfDoc = shallowRef(null);
const numPages = ref(0);
const scrollContainer = ref(null);
const pageViewports = ref({});
const canvasRefs = ref({});

// Scale factor - 1.5 usually gives crisp text on standard screens
const scale = ref(1.5);

const setCanvasRef = (el, pageNum) => {
  if (el) {
    canvasRefs.value[pageNum] = el;
  }
};

const getPageWidth = (pageNum) => {
  return pageViewports.value[pageNum]?.width || 0;
};

const getPageHeight = (pageNum) => {
  return pageViewports.value[pageNum]?.height || 0;
};

const getPageViewport = (pageNum) => {
  return pageViewports.value[pageNum];
};

const getHighlightsForPage = (pageNum) => {
  if (!props.highlights) return [];
  const pageHighlight = props.highlights.find(h => h.page === pageNum);
  return pageHighlight ? pageHighlight.rects : [];
};

const loadPdf = async () => {
  if (!props.url) return;
  
  loading.value = true;
  error.value = null;
  numPages.value = 0;
  pageViewports.value = {};
  canvasRefs.value = {};
  
  try {
    const loadingTask = pdfjsLib.getDocument(props.url);
    const doc = await loadingTask.promise;
    pdfDoc.value = doc;
    numPages.value = doc.numPages;
    
    // Pre-fetch viewports to set layout dimensions immediately
    const viewportPromises = [];
    for (let i = 1; i <= doc.numPages; i++) {
      viewportPromises.push(doc.getPage(i).then(page => {
        const viewport = page.getViewport({ scale: scale.value });
        pageViewports.value[i] = viewport;
        return page;
      }));
    }
    
    await Promise.all(viewportPromises);
    
    // Render pages after DOM update
    await nextTick();
    renderAllPages();
    
  } catch (err) {
    console.error('Error loading PDF:', err);
    error.value = 'Failed to load PDF document.';
  } finally {
    loading.value = false;
  }
};

const renderAllPages = async () => {
  if (!pdfDoc.value) return;
  
  for (let i = 1; i <= numPages.value; i++) {
    renderPage(i);
  }
};

const renderPage = async (pageNum) => {
  try {
    const page = await pdfDoc.value.getPage(pageNum);
    const canvas = canvasRefs.value[pageNum];
    if (!canvas) return;
    
    const viewport = pageViewports.value[pageNum];
    const context = canvas.getContext('2d');
    
    canvas.height = viewport.height;
    canvas.width = viewport.width;
    
    const renderContext = {
      canvasContext: context,
      viewport: viewport
    };
    
    await page.render(renderContext).promise;
  } catch (err) {
    console.error(`Error rendering page ${pageNum}:`, err);
  }
};

const scrollToPage = (pageNum) => {
  const el = document.getElementById(`pdf-page-${pageNum}`);
  if (el) {
    el.scrollIntoView({ behavior: 'smooth', block: 'start' });
  }
};

// Expose method for parent components
defineExpose({
  scrollToPage
});

watch(() => props.url, () => {
  loadPdf();
});

onMounted(() => {
  loadPdf();
});
</script>

<style scoped>
.pdf-viewer-container {
  display: flex;
  flex-direction: column;
  height: 100%;
  width: 100%;
  background: var(--bg-color-soft);
  overflow: hidden;
  position: relative;
}

.pdf-header {
  padding: 12px 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid var(--border-color);
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(10px);
  z-index: 10;
}

.pdf-title {
  font-weight: 600;
  color: var(--text-primary);
  font-size: 0.95rem;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.page-count {
  font-size: 0.85rem;
  color: var(--text-secondary);
  background: rgba(0, 0, 0, 0.05);
  padding: 4px 10px;
  border-radius: 12px;
}

.pdf-scroll-area {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 24px;
}

.pdf-page-wrapper {
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
}

.pdf-page-content {
  position: relative;
  box-shadow: var(--shadow-md);
  background: white;
  transition: transform 0.3s ease;
}

.pdf-canvas {
  display: block;
  width: 100%;
  height: 100%;
}

.highlight-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none; /* Let clicks pass through to text selection if we add text layer later */
}

.highlight-rect {
  fill: rgba(251, 191, 36, 0.3); /* Amber-400 with opacity */
  stroke: rgba(245, 158, 11, 0.8); /* Amber-500 */
  stroke-width: 1;
  rx: 2px; /* Rounded corners */
  animation: pulse-highlight 2s infinite;
}

.page-number {
  font-size: 0.75rem;
  color: var(--text-tertiary);
  font-weight: 500;
}

.error-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  color: var(--danger-color);
  gap: 10px;
}

.error-icon {
  font-size: 2rem;
}

/* Custom Scrollbar */
.custom-scrollbar::-webkit-scrollbar {
  width: 8px;
}
.custom-scrollbar::-webkit-scrollbar-track {
  background: transparent;
}
.custom-scrollbar::-webkit-scrollbar-thumb {
  background-color: rgba(156, 163, 175, 0.5);
  border-radius: 20px;
  border: 3px solid transparent;
  background-clip: content-box;
}
.custom-scrollbar::-webkit-scrollbar-thumb:hover {
  background-color: rgba(107, 114, 128, 0.8);
}

@keyframes pulse-highlight {
  0% { fill-opacity: 0.2; }
  50% { fill-opacity: 0.4; }
  100% { fill-opacity: 0.2; }
}
</style>
