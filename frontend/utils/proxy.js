const fs = require('fs');
function onProxyRes(proxyReq, req, res) {
	const checkAuth = str => !!str.match(/authorization/gm);
	const DEFAULTCOLOR = COLORS.FgYellow;
	const c = (ph, c = DEFAULTCOLOR) => `${c}${ph}\x1b[0m`;
	const statusC = c => c < 400 ? COLORS.FgGreen : COLORS.FgRed;
	const row1 = `[${req.method}] ${c(req.url)} — ${c(res.statusCode, statusC(res.statusCode))} — ${c(req.headers.cookie, COLORS.FgCyan)}`;
	console.log(row1);
}

const COLORS = {
	FgBlack: "\x1b[30m",
	FgRed: "\x1b[31m",
	FgGreen: "\x1b[32m",
	FgYellow: "\x1b[33m",
	FgBlue: "\x1b[34m",
	FgMagenta: "\x1b[35m",
	FgCyan: "\x1b[36m",
	FgWhite: "\x1b[37m",

	BgBlack: "\x1b[40m",
	BgRed: "\x1b[41m",
	BgGreen: "\x1b[42m",
	BgYellow: "\x1b[43m",
	BgBlue: "\x1b[44m",
	BgMagenta: "\x1b[45m",
	BgCyan: "\x1b[46m",
	BgWhite: "\x1b[47m"
};

const config = JSON.parse(fs.readFileSync('proxy.config.json', 'utf-8'));
Object.keys(config).forEach(item => config[item]['onProxyRes'] = onProxyRes);

module.exports = config;
