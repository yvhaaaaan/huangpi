# 黄陂镇特色产业客家文旅非遗小程序

一个微信小程序统一承载普通用户端、商家端和政府管理员端，登录后由后端返回角色并进入对应前端。

## 项目目录

- `小程序/miniprogram`：微信小程序正式前端
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

前端接口配置位于 `小程序/miniprogram/config/api.ts`：

- 开发演示保持 `useMock: true`
- 后端联调时填写已配置为小程序合法域名的 HTTPS `baseUrl`
- 将 `useMock` 改为 `false` 后，登录会使用真实接口

统一请求封装位于 `小程序/miniprogram/utils/request.ts`，业务接口按普通用户、产品、商家、政府审核和文件上传拆分在 `小程序/miniprogram/api/` 中。

## 开发者工具排错

如果控制台连续出现 `App is not defined`、`Page is not defined` 或 `subscribeHandler injected failed`，检查 `小程序/project.private.config.json`，确保 `setting.compileHotReLoad` 为 `false`。私有配置会覆盖仓库中的 `project.config.json`，修改后需要关闭并重新打开项目，或执行“清除缓存并重新编译”。

后端本地启动：

```powershell
cd backend
mvn spring-boot:run
```
