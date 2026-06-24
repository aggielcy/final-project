const symbol = new URLSearchParams(window.location.search).get('symbol');

function calculateSMA(closes, period) {
  return closes.map((_, i) => {
    if (i < period - 1) return null;
    const slice = closes.slice(i - period + 1, i + 1);
    return slice.reduce((a, b) => a + b, 0) / period;
  });
}

async function loadStock() {
  if (!symbol) { document.body.innerHTML = '<p>No symbol provided.</p>'; return; }

  const res = await fetch(`/ui/ohlc?symbol=${symbol}`);
  const stock = await res.json();

  document.getElementById('company-name').textContent = stock.companyName || symbol;
  document.getElementById('company-industry').textContent = stock.industry || '';
  document.getElementById('company-marketcap').textContent =
    'Market Cap: $' + (parseFloat(stock.marketCap) / 1000).toFixed(0) + 'B';
  if (stock.logo) document.getElementById('company-logo').src = stock.logo;
  document.getElementById('candlestick-title').textContent =
    `${stock.companyName} (${symbol})`;

  const twoYearsAgo = new Date();
  twoYearsAgo.setFullYear(twoYearsAgo.getFullYear() - 2);

  const ohlcs = stock.stockOhlcDatas.filter(d => new Date(d.tradeDate) >= twoYearsAgo);

  const dates  = ohlcs.map(d => d.tradeDate);
  const opens  = ohlcs.map(d => d.open);
  const highs  = ohlcs.map(d => d.high);
  const lows   = ohlcs.map(d => d.low);
  const closes = ohlcs.map(d => d.close);

  const candlestick = {
    type: 'candlestick',
    x: dates, open: opens, high: highs, low: lows, close: closes,
    name: symbol,
    increasing: { line: { color: '#00cc44' } },
    decreasing: { line: { color: '#ff3333' } }
  };

  Plotly.newPlot('candlestick', [candlestick,
    { type: 'scatter', mode: 'lines', x: dates, y: calculateSMA(closes, 10), name: 'SMA(10)', line: { color: '#ff4444', width: 1 } },
    { type: 'scatter', mode: 'lines', x: dates, y: calculateSMA(closes, 20), name: 'SMA(20)', line: { color: '#ffaa00', width: 1 } },
    { type: 'scatter', mode: 'lines', x: dates, y: calculateSMA(closes, 50), name: 'SMA(50)', line: { color: '#ffffff', width: 1 } }
  ], {
    paper_bgcolor: '#1a1a2e',
    plot_bgcolor: '#1a1a2e',
    font: { color: '#eaeaea' },
    xaxis: { rangeslider: { visible: false }, gridcolor: '#333' },
    yaxis: { gridcolor: '#333' },
    legend: { bgcolor: '#16213e' },
    margin: { t: 20, l: 60, r: 20, b: 40 },
    height: Math.max(500, window.innerHeight - 250)
  }, { responsive: true });
}

loadStock();