const fs = require('fs')
const path = require('path')
const esbuild = require('esbuild')
const sass = require('sass')

const frontendRoot = path.resolve(__dirname, '..')
const sourceRoot = path.join(frontendRoot, 'miniprogram-src')
const outputRoot = path.join(frontendRoot, 'miniprogram')
const watchMode = process.argv.includes('--watch')

function walkFiles(directory, extension) {
  if (!fs.existsSync(directory)) return []

  return fs.readdirSync(directory, { withFileTypes: true }).flatMap((entry) => {
    const fullPath = path.join(directory, entry.name)
    if (entry.isDirectory()) return walkFiles(fullPath, extension)
    return entry.isFile() && path.extname(entry.name) === extension ? [fullPath] : []
  })
}

function removeGeneratedFiles(directory) {
  if (!fs.existsSync(directory)) return

  for (const entry of fs.readdirSync(directory, { withFileTypes: true })) {
    const fullPath = path.join(directory, entry.name)
    if (entry.isDirectory()) {
      removeGeneratedFiles(fullPath)
      continue
    }

    if (entry.isFile() && ['.js', '.wxss'].includes(path.extname(entry.name))) {
      fs.unlinkSync(fullPath)
    }
  }
}

function compileTypeScript(files) {
  if (files.length === 0) return

  esbuild.buildSync({
    entryPoints: files,
    outbase: sourceRoot,
    outdir: outputRoot,
    bundle: false,
    charset: 'utf8',
    format: 'cjs',
    legalComments: 'none',
    logLevel: 'silent',
    platform: 'neutral',
    sourcemap: false,
    target: ['es2017'],
  })
}

function compileSass(files) {
  for (const file of files) {
    const relativePath = path.relative(sourceRoot, file).replace(/\.scss$/i, '.wxss')
    const outputPath = path.join(outputRoot, relativePath)
    const result = sass.compile(file, {
      loadPaths: [sourceRoot],
      style: 'expanded',
    })

    fs.mkdirSync(path.dirname(outputPath), { recursive: true })
    fs.writeFileSync(outputPath, result.css, 'utf8')
  }
}

function build() {
  const startedAt = Date.now()
  const typeScriptFiles = walkFiles(sourceRoot, '.ts').sort()
  const sassFiles = walkFiles(sourceRoot, '.scss').sort()

  removeGeneratedFiles(outputRoot)
  compileTypeScript(typeScriptFiles)
  compileSass(sassFiles)

  const elapsed = Date.now() - startedAt
  console.log(`小程序构建完成：${typeScriptFiles.length} 个 JS，${sassFiles.length} 个 WXSS（${elapsed}ms）`)
}

build()

if (watchMode) {
  let timer = null
  console.log('正在监听 miniprogram-src，按 Ctrl+C 停止。')
  fs.watch(sourceRoot, { recursive: true }, () => {
    clearTimeout(timer)
    timer = setTimeout(() => {
      try {
        build()
      } catch (error) {
        console.error(error)
      }
    }, 100)
  })
}
