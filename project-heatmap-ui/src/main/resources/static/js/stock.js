const symbol = new URLSearchParams(window.location.search).get('symbol');
let allOhlcDatas = [];

function calculateSMA(closes, period) {
  return closes.map((_, i) => {
    if (i < period - 1) return null;
    const slice = closes.slice(i - period + 1, i + 1);
    return slice.reduce((a, b) => a + b, 0) / period;
  });
}

function formatMarketCap(val) {
  const n = parseFloat(val);
  if (!n) return '—';
  if (n >= 1000000) return (n / 1000000).toFixed(3) + 'T';
  if (n >= 1000) return (n / 1000).toFixed(2) + 'B';
  return n.toFixed(0) + 'M';
}

function formatShares(val) {
  const n = parseFloat(val);
  if (!n) return '—';
  if (n >= 1000) return (n / 1000).toFixed(2) + 'B';
  return n.toFixed(0) + 'M';
}

function filterByRange(range) {
  const cutoff = new Date();
  if (range === '1W') cutoff.setDate(cutoff.getDate() - 7);
  else if (range === '1M') cutoff.setMonth(cutoff.getMonth() - 1);
  else if (range === '3M') cutoff.setMonth(cutoff.getMonth() - 3);
  else if (range === '1Y') cutoff.setFullYear(cutoff.getFullYear() - 1);
  return allOhlcDatas.filter(d => new Date(d.tradeDate) >= cutoff);
}

function renderChart(ohlcs) {
  const dates  = ohlcs.map(d => d.tradeDate);
  const opens  = ohlcs.map(d => parseFloat(d.open));
  const highs  = ohlcs.map(d => parseFloat(d.high));
  const lows   = ohlcs.map(d => parseFloat(d.low));
  const closes = ohlcs.map(d => parseFloat(d.close));

  Plotly.react('candlestick', [
    {
      type: 'candlestick',
      x: dates, open: opens, high: highs, low: lows, close: closes,
      name: symbol,
      increasing: { line: { color: '#04c704' } },
      decreasing: { line: { color: '#e70909' } }
    },
    { type: 'scatter', mode: 'lines', x: dates, y: calculateSMA(closes, 10), name: 'SMA 10', line: { color: '#ff4444', width: 1 } },
    { type: 'scatter', mode: 'lines', x: dates, y: calculateSMA(closes, 20), name: 'SMA 20', line: { color: '#ffaa00', width: 1 } },
    { type: 'scatter', mode: 'lines', x: dates, y: calculateSMA(closes, 50), name: 'SMA 50', line: { color: '#aaaaaa', width: 1 } }
  ], {
    paper_bgcolor: '#0e0e1a',
    plot_bgcolor: '#0e0e1a',
    font: { color: '#eaeaea' },
    xaxis: { rangeslider: { visible: false }, gridcolor: '#1f1f35', linecolor: '#1f1f35' },
    yaxis: { gridcolor: '#1f1f35', side: 'right' },
    legend: { bgcolor: 'transparent', x: 0, y: 1 },
    margin: { t: 20, l: 20, r: 60, b: 40 },
    height: Math.max(450, window.innerHeight - 320)
  }, { responsive: true });
}

async function loadStock() {
  if (!symbol) { document.body.innerHTML = '<p style="color:#fff;padding:40px">No symbol provided.</p>'; return; }

  const res = await fetch(`/ui/cache/ohlc?symbol=${symbol}`);
  const stock = await res.json();

  document.title = `${symbol} - Stock Detail`;
  document.getElementById('company-name').textContent = stock.companyName || symbol;
  document.getElementById('stock-symbol-label').textContent = symbol;
  const logoEl = document.getElementById('company-logo');
  const logoWrapper = document.getElementById('logo-wrapper');
  if (stock.logo) {
    logoEl.src = stock.logo;
    logoEl.onerror = () => { logoWrapper.style.display = 'none'; };
  } else {
    logoWrapper.style.display = 'none';
  }

  allOhlcDatas = stock.stockOhlcDatas || [];

  // Price + change
  const closes = allOhlcDatas.map(d => parseFloat(d.close));
  const latestClose = closes[closes.length - 1];
  const pct = stock.currentPriceChangePercent != null
    ? parseFloat(stock.currentPriceChangePercent)
    : ((latestClose - closes[closes.length - 2]) / closes[closes.length - 2] * 100);
  const changeAmt = latestClose - (latestClose / (1 + pct / 100));
  const pctColor = pct >= 0 ? '#04c704' : '#e70909';

  document.getElementById('stock-price').textContent = latestClose.toFixed(2);
  document.getElementById('stock-change-amt').textContent = (changeAmt >= 0 ? '+' : '') + changeAmt.toFixed(2);
  document.getElementById('stock-change-pct').textContent = '(' + (pct >= 0 ? '+' : '') + pct.toFixed(2) + '%)';
  document.getElementById('stock-change-amt').style.color = pctColor;
  document.getElementById('stock-change-pct').style.color = pctColor;

  // Stats
  document.getElementById('stat-marketcap').textContent = formatMarketCap(stock.marketCap);
  document.getElementById('stat-industry').textContent = stock.industry || '—';
  document.getElementById('stat-shares').textContent = formatShares(stock.shareOutstanding);

  const oneYearAgo = new Date();
  oneYearAgo.setFullYear(oneYearAgo.getFullYear() - 1);
  const ohlcs52w = allOhlcDatas.filter(d => new Date(d.tradeDate) >= oneYearAgo);
  if (ohlcs52w.length) {
    const h52 = Math.max(...ohlcs52w.map(d => parseFloat(d.high))).toFixed(2);
    const l52 = Math.min(...ohlcs52w.map(d => parseFloat(d.low))).toFixed(2);
    document.getElementById('stat-52w').textContent = `${l52} – ${h52}`;
  }

  // Default chart: 1Y
  renderChart(filterByRange('1Y'));

  // Button click handlers
  document.querySelectorAll('.range-btn').forEach(btn => {
    btn.addEventListener('click', () => {
      document.querySelectorAll('.range-btn').forEach(b => b.classList.remove('active'));
      btn.classList.add('active');
      renderChart(filterByRange(btn.dataset.range));
    });
  });
}

loadStock();