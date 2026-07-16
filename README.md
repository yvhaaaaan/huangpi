# 黄陂镇特色产业客家文旅非遗小程序

一个微信小程序统一承载普通用户端、商家端和政府管理员端，登录后由后端返回角色并进入对应前端。

## 项目目录

- `frontend`：微信小程序前端工程，开发者工具直接打开此目录
- `frontend/miniprogram-src`：TypeScript 和 SCSS 业务源码
- `frontend/miniprogram`：构建后的原生 JS/WXSS 运行目录，微信开发者工具只加载此目录
- `backend`：Java 17 + Spring Boot 后端
- `黄陂镇油茶客家文旅非遗小程序_前端PRD.md`：前端需求文档
- `黄陂镇油茶客家文旅非遗小程序_后端PRD.md`：后端需求文档
- `黄陂镇油茶客家文旅非遗小程序需求说明书(1).md`：项目需求说明

## 当前角色

- 普通用户：微信登录，浏览特色产品、文旅、非遗、地图和活动
- 商家：账号密码登录，使用“我的”和“新增”管理特色产品
- 政府管理员：账号密码登录，使用工作台和内容审核

油茶与丝苗米均为一级重点特色产品，后续品类通过统一产品模型扩展。

## 后端当前进度

Spring Boot MVP 已实现统一认证、三角色权限、商家产品审核闭环、文件上传、地图路线、平台活动、活动报名和收藏。活动由平台/政府侧维护，商家端没有活动管理接口。

验证命令：

```powershell
cd backend
mvn clean package
```

## 后端接口切换

前端接口配置位于 `frontend/miniprogram-src/config/api.ts`：

- 开发演示保持 `useMock: true`
- 后端联调时填写已配置为小程序合法域名的 HTTPS `baseUrl`
- 将 `useMock` 改为 `false` 后，登录会使用真实接口

统一请求封装位于 `frontend/miniprogram-src/utils/request.ts`，业务接口按普通用户、产品、商家、政府审核和文件上传拆分在 `frontend/miniprogram-src/api/` 中。

## 前端开发

首次拉取后安装依赖并构建：

```powershell
cd frontend
npm install
npm run check:miniprogram
```

日常开发时修改 `miniprogram-src` 中的 `.ts` 和 `.scss`，同时运行监听构建：

```powershell
npm run watch:miniprogram
```

`miniprogram` 中的 `.js` 和 `.wxss` 是生成文件，不要直接修改。

## 开发者工具排错

项目已关闭微信开发者工具内置的 TypeScript/Sass 编译插件，避免其热注入链路在 `App`、`Page` 初始化前直接执行源码。开发者工具应打开 `frontend`，运行根目录由 `project.config.json` 固定为 `miniprogram/`；该目录中不应出现 `.ts` 或 `.scss`。如果重新拉取后缺少 `.js`/`.wxss`，先执行 `npm run build:miniprogram`，再重新编译项目。

项目基础库固定为稳定版 `3.7.12`。不要选择 `trial` 灰度基础库：旧版开发者工具加载 `3.16.2` 等较新运行库时，可能导致调试运行库注入失败，并连锁出现 `App is not defined`、`Page is not defined`。如需使用更新基础库，应先升级微信开发者工具。

后端本地启动：

```powershell
cd backend
mvn spring-boot:run
```
