const path = require('path')
const { defineConfig } = require('vite')
const vue = require('@vitejs/plugin-vue')
const uniH5 = require('@dcloudio/uni-h5-vite')

const inputDir = path.resolve(__dirname, 'src')

process.env.UNI_PLATFORM = 'h5'
process.env.UNI_INPUT_DIR = inputDir
process.env.UNI_OUTPUT_DIR = path.resolve(__dirname, 'dist')
process.env.UNI_CLI_CONTEXT = __dirname
process.env.UNI_COMPILER = 'vite'
process.env.UNI_APP_X = process.env.UNI_APP_X || 'false'

const uniPlugins = uniH5.default ? uniH5.default() : uniH5()

module.exports = defineConfig({
  root: inputDir,
  resolve: {
    alias: {
      '@': inputDir,
    },
  },
  plugins: [
    ...uniPlugins,
    vue(),
  ],
  server: {
    host: '127.0.0.1',
    port: 5173,
  },
  build: {
    outDir: path.resolve(__dirname, 'dist'),
    emptyOutDir: true,
  },
})
