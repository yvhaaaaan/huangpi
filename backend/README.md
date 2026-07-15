# 后端服务

Spring Boot 后端位于本目录，使用 Java 17、Maven、Spring Security、Spring Data JPA 和 JWT。

## 已实现能力

- 强制登录与 `user / merchant / admin` 三角色权限隔离
- 微信登录、账号密码登录、JWT 会话校验与退出失效
- 油茶、丝苗米等统一产品品类和商家产品审核闭环
- 商家资料、产品、消息、待办接口
- 政府审核、品类、商家和审计日志接口
- 地图点位、推荐路线、平台活动、活动报名和收藏接口
- JPG/PNG/WebP 图片上传与本地开发存储

## 本地启动

```powershell
cd backend
mvn spring-boot:run
```

默认地址：`http://localhost:8080`

- 健康检查：`GET /actuator/health`
- H2 控制台：`http://localhost:8080/h2-console`
- JDBC URL：`jdbc:h2:file:./data/huangpi;MODE=MySQL;AUTO_SERVER=TRUE`
- 用户名：`sa`
- 密码：空

## 开发账号

| 角色 | 账号 | 密码 |
| --- | --- | --- |
| 商家 | `merchant` | `123456` |
| 政府管理员 | `admin` | `123456` |

开发环境默认开启微信登录 mock，任意非空 `code` 会生成稳定的测试普通用户。

## 测试与打包

```powershell
cd backend
mvn test
mvn clean package
```

端到端测试覆盖商家提交产品、政府审核、普通用户查看、活动报名、收藏、角色越权和退出登录后的 token 失效。

## 生产环境

使用 `prod` profile，并配置：

```text
DB_URL=jdbc:mysql://host:3306/huangpi?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
DB_USERNAME=...
DB_PASSWORD=...
JWT_SECRET=至少32字节随机密钥
WECHAT_APP_ID=...
WECHAT_APP_SECRET=...
STORAGE_DIRECTORY=/data/huangpi/uploads
PUBLIC_BASE_URL=https://api.example.com
```

启动命令：

```powershell
mvn clean package
java -jar target/huangpi-backend-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```
