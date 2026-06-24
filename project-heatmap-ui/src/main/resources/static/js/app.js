async function loadHeatmap() {
  const res = await fetch('/ui/symbols');
  const data = await res.json();
  const symbols = data.symbol.slice(0, 500);

  const ohlcResults = await Promise.all(
    symbols.map(symbol =>
      fetch(`/ui/ohlc?symbol=${symbol}`)
        .then(r => r.json())
        .catch(() => null)
    )
  );

  const labels = ['Market'];
  const parents = [''];
  const values = [0];
  const colors = [0];
  const texts = [''];
 

  const industriesAdded = new Set();

  ohlcResults.forEach(stock => {
    if (!stock || !stock.stockOhlcDatas || stock.stockOhlcDatas.length < 2) return;

    const industry = stock.industry || 'Other';

    if (!industriesAdded.has(industry)) {
      labels.push(industry);
      parents.push('Market');
      values.push(0);
      colors.push(0);
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
  });

  const trace = {
    type: 'treemap',
    labels: labels,
    parents: parents,
    values: values,
    text: texts,
    textinfo: 'text',
    textposition: 'middle center',
    textfont: { size: 20 },
    hovertemplate: '<b>%{label}</b><br>%{text}<extra></extra>',
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
      showscale:false
    }
  };

  Plotly.newPlot('heatmap', [trace], {
    paper_bgcolor: '#1a1a2e',
    plot_bgcolor: '#1a1a2e',
    font: { color: '#eaeaea', size: 11 },
    margin: { t: 0, l: 0, r: 80, b: 0 },
    height: Math.max(550, window.innerHeight - 180)
  }, { responsive: true });

document.getElementById('heatmap').on('plotly_click', function (eventData) {
    const point = eventData.points[0];
    const isStock = point.parent !== 'Market' && point.parent !== '';
    if (isStock) {
      window.location.href = `/stock.html?symbol=${point.label}`;
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

async function loadCandlestick(symbol) {
  const res = await fetch(`/ui/ohlc?symbol=${symbol}`);
  const stock = await res.json();

  document.getElementById('stock-info').style.display = 'flex';
  document.getElementById('company-name').textContent = stock.companyName || symbol;
  document.getElementById('company-industry').textContent = stock.industry || '';
  document.getElementById('company-marketcap').textContent =
    'Market Cap: $' + (parseFloat(stock.marketCap) / 1000).toFixed(0) + 'B';
  if (stock.logo) document.getElementById('company-logo').src = stock.logo;

  const ohlcs = stock.stockOhlcDatas;
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

  const sma10 = {
    type: 'scatter', mode: 'lines',
    x: dates, y: calculateSMA(closes, 10),
    name: 'SMA(10)', line: { color: '#ff4444', width: 1 }
  };
  const sma20 = {
    type: 'scatter', mode: 'lines',
    x: dates, y: calculateSMA(closes, 20),
    name: 'SMA(20)', line: { color: '#ffaa00', width: 1 }
  };
  const sma50 = {
    type: 'scatter', mode: 'lines',
    x: dates, y: calculateSMA(closes, 50),
    name: 'SMA(50)', line: { color: '#ffffff', width: 1 }
  };

  document.getElementById('candlestick-title').textContent =
    `${stock.companyName} (${symbol})`;

  Plotly.newPlot('candlestick', [candlestick, sma10, sma20, sma50], {
    paper_bgcolor: '#1a1a2e',
    plot_bgcolor: '#1a1a2e',
    font: { color: '#eaeaea' },
    xaxis: { rangeslider: { visible: false }, gridcolor: '#333' },
    yaxis: { gridcolor: '#333' },
    legend: { bgcolor: '#16213e' },
    margin: { t: 20, l: 60, r: 20, b: 40 }
  });
}

loadHeatmap();
setInterval(loadHeatmap, 60000);