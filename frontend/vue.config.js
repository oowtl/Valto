
// Vue3 관련 설정 파일
module.exports = {
  devServer: {
    https: true,
    port: 8083,
    open: true,
    proxy: {
      '/api/v1': {
        target: 'https://backend:443/'
      },
      '/webjars': {
        target: 'https://backend:443/'
      },
      '/group-call': {
        target: 'https://backend:443/'
      },
      '/upload': {
        target: 'https://backend:443/'
      },

    },
    historyApiFallback: true,
    hot: true,
    host: 'localhost',
  },
  css: {
    requireModuleExtension: false // import 시에 경로에 .module 포함 안해도 됨.
  },
  transpileDependencies: [
    'element-plus'
  ],
  lintOnSave: true,
  outputDir: './dist'
}
