# 水果店管理系统

一个基于Java的现代化应用程序，用于管理水果店的库存、销售和日常运营。

## 项目概述

水果店管理系统是一个功能齐全的桌面应用程序，旨在简化水果店的日常运营。该系统提供库存跟踪、销售处理、客户管理和财务报表等功能，帮助店主更好地管理业务。

## 功能特性

- **库存管理**：实时跟踪水果库存数量，设置最低库存警报
- **销售跟踪**：记录每笔销售交易，生成销售报告
- **产品目录**：管理水果种类、价格和供应商信息
- **客户管理**：维护客户信息，支持会员制度
- **库存监控**：自动提醒即将过期的水果，减少浪费
- **财务报表**：生成日、周、月度财务报告
- **数据备份**：定期备份重要业务数据

## 使用的技术

- **Java**：核心编程语言
- **Swing/JavaFX**：图形用户界面
- **JDBC/SQLite**：数据库连接与存储
- **Maven**：依赖管理和构建工具
- **JUnit**：单元测试框架
- **Log4j**：日志记录

## 快速开始

### 前置要求

- Java Development Kit (JDK) 8 或更高版本
- Apache Maven 3.6 或更高版本
- Git (用于版本控制)
- SQLite (数据库)

### 环境配置

确保您的系统已正确安装并配置了Java环境变量。

### 安装

1. 克隆仓库到本地：
   ```bash
   git clone https://github.com/yourusername/fruit-shop-management.git
   ```
   
2. 导航到项目目录：
   ```bash
   cd fruit-shop-management
   ```

3. 使用Maven构建项目：
   ```bash
   mvn clean install
   ```

4. 初始化数据库：
   ```bash
   # 运行初始化脚本
   mvn exec:java -Dexec.mainClass="com.fruitshop.util.DatabaseInitializer"
   ```

### 运行应用程序

首次运行前，请确保已完成数据库初始化。

1. 直接运行主类：
   ```bash
   mvn exec:java -Dexec.mainClass="com.fruitshop.Main"
   ```

2. 或者打包后运行：
   ```bash
   mvn package
   java -jar target/fruit-shop-management.jar
   ```

### 系统配置

首次启动时，系统会引导您完成基本配置，包括：
- 店铺信息设置
- 初始库存录入
- 用户账户创建

## 项目结构

```
fruit-shop-management/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── fruitshop/
│   │   │           ├── controller/      # 控制器层
│   │   │           ├── model/          # 数据模型
│   │   │           ├── view/           # 视图组件
│   │   │           ├── dao/            # 数据访问对象
│   │   │           ├── service/        # 业务逻辑层
│   │   │           ├── util/           # 工具类
│   │   │           └── Main.java       # 主入口类
│   │   └── resources/
│   │       ├── database/               # 数据库相关资源
│   │       ├── config/                 # 配置文件
│   │       └── images/                 # 图片资源
│   └── test/
│       └── java/                       # 测试代码
├── docs/                               # 项目文档
├── target/                             # 构建输出目录
├── pom.xml                            # Maven配置文件
└── README.md                          # 项目说明文档
```

## 开发指南

### 代码规范

- 遵循Java编码规范
- 类名使用PascalCase命名法
- 方法和变量使用camelCase命名法
- 所有代码必须包含适当的注释

### 提交规范

- 功能开发：`feat: 添加新功能`
- 修复bug：`fix: 修复问题描述`
- 文档更新：`docs: 更新文档`
- 重构代码：`refactor: 重构说明`

## 测试

运行所有单元测试：
```bash
mvn test
```

运行特定测试类：
```bash
mvn test -Dtest=TestClass
```

## 贡献

我们欢迎各种形式的贡献！如果您想为项目做出贡献，请遵循以下步骤：

1. Fork 本仓库
2. 创建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

在提交之前，请确保：
- 代码符合我们的编码规范
- 添加了适当的单元测试
- 所有测试都通过
- 更新了相关的文档

## 版本历史

- v1.0.0：初始版本，实现基础库存和销售功能
- v1.1.0：添加客户管理和报表功能
- v1.2.0：优化UI界面，增加数据导出功能

## 致谢

- 感谢所有为本项目贡献代码的开发者
- 感谢社区提供的优秀开源库和工具

## 支持

如有任何问题或建议，请通过以下方式联系我们：

- 提交 Issue
- 发送邮件至 [邮箱地址]

## 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](./LICENSE) 文件了解详情。
