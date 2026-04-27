# 在线手办购买系统

基于 Spring Boot + Vue 的前后端分离电商项目，目前已实现用户登录注册、购物车添加、订单管理等核心功能。

---

## 技术栈

### 后端
- Spring Boot
- MyBatis / MyBatis-Plus
- MySQL
- Maven

### 前端
- Vue 3 / Vue 2
- Element Plus / Element UI
- Axios

---

## 项目结构

backend/         # Spring Boot 后端项目
  ├── src/       # 后端业务代码（Controller/Service/Mapper）
  ├── pom.xml    # 后端依赖配置
  └── application.yml  # 数据库/服务配置

frontend/        # Vue 前端项目
  ├── src/       # 前端页面与组件
  ├── package.json  # 前端依赖配置
  └── vite.config.js / vue.config.js  # 前端配置

README.md        # 项目说明文档

---

## 启动步骤

### 后端启动
1.  导入项目的 SQL 文件到 MySQL 数据库。
2.  修改 backend/src/main/resources/application.yml 中的数据库配置：
    spring:
      datasource:
        url: jdbc:mysql://localhost:3306/figure_shop?useSSL=false&serverTimezone=UTC
        username: root
        password: 你的数据库密码
3.  使用 IDEA 打开 backend 项目，执行 mvn clean package 进行编译。
4.  启动 Spring Boot 主类，后端默认运行在 http://localhost:8080。

### 前端启动
1.  打开终端，进入 frontend 目录。
2.  安装依赖：
    npm install
3.  启动项目：
    如果是 Vue 3 / Vite 项目：npm run dev
    如果是 Vue 2 / Webpack 项目：npm run serve
4.  前端默认运行在 http://localhost:5173 或 http://localhost:8081，访问即可使用。

---

## 已实现功能
- 用户模块：账号登录、注册
- 购物车模块：手办商品添加到购物车
- 订单模块：订单创建与管理

---

## 后续规划
- 商品详情与分类展示
- 购物车商品修改/删除
- 订单支付与状态流转
- 管理员后台商品管理
