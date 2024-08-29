const path = require('path');
const { override } = require('customize-cra');

module.exports = override(
    (config, env) => {
        config.plugins = config.plugins.map(plugin => {
            if (plugin.constructor.name === 'HtmlWebpackPlugin') {
                // 상대 경로를 사용하여 index.html 파일 경로를 설정합니다.
                plugin.options.template = path.resolve(__dirname, 'app/Frontend/public/index.html');
            }
            return plugin;
        });
        return config;
    }
);
