let stockDataMap = {};
let industryMap = {};

async function loadHeatmap() {
  stockDataMap = {};
  industryMap = {};

  const res = await fetch('/ui/cache/all');
  const ohlcResults = await res.json();

  const labels = [];
  const parents = [];
  const values = [];
  const colors = [];
  const texts = [];

  const industriesAdded = new Set();

  ohlcResults.forEach(stock => {
    if (!stock || !stock.stockOhlcDatas || stock.stockOhlcDatas.length < 2) return;

    const industry = stock.industry || 'Other';

    if (!industriesAdded.has(industry)) {
      labels.push(industry);
      parents.push('');
      values.push(0);
      colors.push('#222222');
      texts.push(industry);
      industriesAdded.add(industry);
    }

    const ohlcs = stock.stockOhlcDatas;
    const latest = ohlcs[ohlcs.length - 1];
    const previous = ohlcs[ohlcs.length - 2];
    const historicalPct = ((latest.close - previous.close) / previous.close) * 100;
    const pctChange = (stock.currentPriceChangePercent != null)
                      ? parseFloat(stock.currentPriceChangePercent)
                      : historicalPct;

    labels.push(stock.symbol);
    parents.push(industry);
    values.push(parseFloat(stock.marketCap) || 1);
    colors.push(pctChange);
    texts.push(`${stock.symbol}<br>${pctChange.toFixed(2)}%`);
    stockDataMap[stock.symbol] = stock;
    const ind = stock.industry || 'Other';
    if (!industryMap[ind]) industryMap[ind] = [];
    industryMap[ind].push(stock.symbol);
  });

  const trace = {
    type: 'treemap',
    labels: labels,
    parents: parents,
    values: values,
    text: texts,
    textinfo: 'text',
    textposition: 'middle center',
    textfont: { size: 14 },
    hoverinfo: 'none',
    pathbar: { visible: false },
    marker: {
      colors: colors,
      colorscale: [
        [0, '#e70909'],
        [0.5, '#333232'],
        [1, '#04c704']
      ],
      cmin: -3,
      cmax: 3,
      showscale: false,
      line: {
        color: '#222222',
        width: 0.5
      }
    }
  };

  Plotly.newPlot('heatmap', [trace], {
    paper_bgcolor: '#222222',
    plot_bgcolor: '#222222',
    font: { color: '#eaeaea', size: 11 },
    margin: { t: 0, l: 0, r: 0, b: 0 },
    height: Math.max(550, window.innerHeight - 180)
  }, { responsive: true });

  setTimeout(() => {
    adjustFontSizes();
  }, 300);

  document.getElementById('heatmap').on('plotly_click', function (eventData) {
    const point = eventData.points[0];
    const isStock = point.parent !== '';
    if (isStock) {
      window.location.href = `/stock.html?symbol=${point.label}`;
    }
  });

  document.getElementById('heatmap').on('plotly_hover', function(eventData) {
    const point = eventData.points[0];
    if (!point.parent) return;
    const stock = stockDataMap[point.label];
    if (!stock) return;
    showTooltip(stock, eventData.event);
  });

  document.getElementById('heatmap').on('plotly_unhover', function() {
    document.getElementById('hover-tooltip').style.display = 'none';
  });
}

function adjustFontSizes() {
  const slices = document.querySelectorAll('#heatmap .slice');
  slices.forEach(slice => {
    const path = slice.querySelector('path.surface');
    const text = slice.querySelector('text');
    if (!path || !text) return;

    const tspans = text.querySelectorAll('tspan');
    const bbox = path.getBBox();

    if (tspans.length <= 1) {
      text.style.display = '';
      text.style.fontSize = '15px';
      text.setAttribute('text-anchor', 'start');
      tspans.forEach(t => t.setAttribute('x', bbox.x + 6));
      return;
    }

    const area = bbox.width * bbox.height;
    const size = (Math.sqrt(area) / 8) * 1.8;

    if (size < 8) {
      text.style.display = 'none';
    } else {
      text.style.display = '';
      const baseSize = Math.min(30, size);
      text.style.fontSize = baseSize + 'px';
      if (tspans[0]) tspans[0].style.fontSize = baseSize + 'px';
      if (tspans[1]) tspans[1].style.fontSize = (baseSize * 0.65) + 'px';
    }
  });
}


function calculateSMA(closes, period) {
  return closes.map((_, i) => {
    if (i < period - 1) return null;
    const slice = closes.slice(i - period + 1, i + 1);
    return slice.reduce((a, b) => a + b, 0) / period;
  });
}


function createSparkline(ohlcDatas, width, height) {
  const last30 = ohlcDatas.slice(-30);
  const closes = last30.map(d => parseFloat(d.close));
  const min = Math.min(...closes);
  const max = Math.max(...closes);
  const range = max - min || 1;
  const points = closes.map((c, i) => {
    const x = (i / (closes.length - 1)) * width;
    const y = height - ((c - min) / range) * height;
    return `${x.toFixed(1)},${y.toFixed(1)}`;
  }).join(' ');
  const color = closes[closes.length - 1] >= closes[0] ? '#04c704' : '#e70909';
  return `<svg width="${width}" height="${height}" style="vertical-align:middle"><polyline points="${points}" fill="none" stroke="${color}" stroke-width="1.5"/></svg>`;
}

function showTooltip(stock, event) {
  const tooltip = document.getElementById('hover-tooltip');
  const ohlcs = stock.stockOhlcDatas;
  const latestClose = ohlcs && ohlcs.length ? parseFloat(ohlcs[ohlcs.length - 1].close) : null;
  const pct = stock.currentPriceChangePercent != null ? parseFloat(stock.currentPriceChangePercent) : null;
  const pctStr = pct != null ? (pct >= 0 ? '+' : '') + pct.toFixed(2) + '%' : '';
  const pctColor = pct != null && pct >= 0 ? '#04c704' : '#e70909';
  const sparkline = ohlcs ? createSparkline(ohlcs, 80, 30) : '';
  const priceStr = latestClose != null ? latestClose.toFixed(2) : '';

  const peers = (industryMap[stock.industry] || [])
    .map(s => stockDataMap[s]).filter(Boolean)
    .filter(p => p.symbol !== stock.symbol)
    .sort((a, b) => (parseFloat(b.marketCap) || 0) - (parseFloat(a.marketCap) || 0))
    .slice(0, 5);

  const peerRows = peers.map(p => {
    const pClose = p.stockOhlcDatas ? parseFloat(p.stockOhlcDatas[p.stockOhlcDatas.length - 1].close) : 0;
    const pPct = p.currentPriceChangePercent != null ? parseFloat(p.currentPriceChangePercent) : 0;
    const pPctStr = (pPct >= 0 ? '+' : '') + pPct.toFixed(2) + '%';
    const pColor = pPct >= 0 ? '#04c704' : '#e70909';
    const pSpark = p.stockOhlcDatas ? createSparkline(p.stockOhlcDatas, 60, 22) : '';
    return `<div class="tooltip-peer">
      <span class="tooltip-peer-symbol">${p.symbol}</span>
      <span class="tooltip-peer-spark">${pSpark}</span>
      <span class="tooltip-peer-price">${pClose.toFixed(2)}</span>
      <span class="tooltip-peer-pct" style="color:${pColor}">${pPctStr}</span>
    </div>`;
  }).join('');

  tooltip.innerHTML = `
    <div class="tooltip-header">${stock.industry || ''}</div>
    <div class="tooltip-main">
      ${stock.logo ? `<img src="${stock.logo}" referrerpolicy="no-referrer" style="width:48px;height:48px;border-radius:4px;object-fit:contain;background:white;padding:2px;flex-shrink:0;" onerror="this.style.display='none'"/>` : ''}
      <span class="tooltip-symbol">${stock.symbol}</span>
      <span class="tooltip-spark">${sparkline}</span>
      <span class="tooltip-price">${priceStr}</span>
      <span class="tooltip-pct" style="color:${pctColor}">${pctStr}</span>
    </div>
    <div class="tooltip-company">${stock.companyName || ''}</div>
    <div class="tooltip-peers">${peerRows}</div>
  `;

  tooltip.style.left = (event.clientX + 16) + 'px';
  tooltip.style.top = (event.clientY - 10) + 'px';
  tooltip.style.display = 'block';

  const rect = tooltip.getBoundingClientRect();
  if (rect.right > window.innerWidth - 10)
    tooltip.style.left = (event.clientX - rect.width - 16) + 'px';
  if (rect.bottom > window.innerHeight - 10)
    tooltip.style.top = (event.clientY - rect.height + 10) + 'px';
}

loadHeatmap();
// setInterval(loadHeatmap, 60000);