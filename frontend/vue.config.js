
// Vue3 관련 설정 파일
module.exports = {
  devServer: {
    https: true,
    port: 8083,
    open: true,
    proxy: {
      '/api/v1': {
        target: 'https://i5d103.p.ssafy.io:8080/'
      },
      '/webjars': {
        target: 'https://i5d103.p.ssafy.io:8080/'
      },
      '/group-call': {
        target: 'https://i5d103.p.ssafy.io:8080/'
      },
      '/upload': {
        target: 'https://i5d103.p.ssafy.io:8080/'
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
