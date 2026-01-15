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
fruitshop-manager/
├── src/main/java/com/fruitshop/
│   ├── FruitshopApplication.java          # SpringBoot启动类
│   ├── config/                           # 配置类
│   │   ├── AESPasswordEncoder.java       # AES密码编码器（补充）
│   │   ├── CustomAuthenticationProvider.java # 自定义认证提供者（补充）
│   │   ├── DatabaseConfig.java           # 数据源配置
│   │   ├── MyBatisPlusConfig.java        # MyBatis-Plus配置
│   │   ├── SecurityConfig.java           # 安全配置
│   │   ├── TransactionConfig.java        # 事务配置
│   │   └── WebMvcConfig.java             # MVC配置
│   ├── controller/                       # 控制器
│   │   ├── AuthController.java           # 认证控制器
│   │   ├── CustomerController.java       # 顾客控制器
│   │   ├── DashboardController.java      # 仪表板控制器（补充）
│   │   ├── FruitController.java          # 水果控制器
│   │   ├── OperationLogController.java   # 操作日志控制器（补充）
│   │   ├── OrderController.java          # 订单控制器
│   │   ├── PageController.java           # 页面控制器（补充）
│   │   ├── SupplierController.java       # 供应商控制器（补充）
│   │   ├── ViewController.java           # 视图控制器（补充）
│   │   └── VipController.java           # VIP控制器（补充）
│   ├── service/                          # 服务层接口
│   │   ├── AuthService.java              # 认证服务接口
│   │   ├── CustomerService.java          # 顾客服务接口
│   │   ├── FruitService.java             # 水果服务接口
│   │   ├── OperationLogService.java      # 操作日志服务接口（补充）
│   │   ├── OrderService.java             # 订单服务接口
│   │   └── VipService.java               # VIP服务接口
│   ├── service/impl/                     # 服务层实现类
│   │   ├── AuthServiceImpl.java          # 认证服务实现
│   │   ├── CustomerServiceImpl.java      # 顾客服务实现
│   │   ├── FruitServiceImpl.java         # 水果服务实现
│   │   ├── OrderServiceImpl.java         # 订单服务实现
│   │   ├── UserDetailsServiceImpl.java   # 用户详情服务实现（补充）
│   │   └── VipServiceImpl.java          # VIP服务实现（补充）
│   ├── dao/                              # 数据访问层（或mapper）
│   │   ├── CustomerMapper.java
│   │   ├── FruitMapper.java
│   │   ├── OperationLogMapper.java
│   │   ├── OrderItemMapper.java
│   │   ├── OrderMapper.java
│   │   ├── RoleMapper.java
│   │   ├── SupplierMapper.java
│   │   └── UserMapper.java
│   ├── entity/                           # 实体类
│   │   ├── Customer.java
│   │   ├── Fruit.java
│   │   ├── OperationLog.java
│   │   ├── Order.java
│   │   ├── OrderItem.java
│   │   ├── Role.java
│   │   ├── Supplier.java
│   │   └── User.java
│   ├── dto/                              # 数据传输对象
│   │   ├── LoginDTO.java
│   │   ├── OrderDTO.java
│   │   ├── OrderItemDTO.java
│   │   └── VipDiscountDTO.java
│   ├── vo/                               # 视图对象
│   │   ├── CustomerVO.java
│   │   ├── OrderItemVO.java
│   │   └── OrderVO.java
│   ├── util/                             # 工具类
│   │   ├── AESUtil.java                  # AES加密工具
│   │   ├── ResponseUtil.java             # 响应工具
│   │   ├── Result.java                   # 统一返回结果（补充）
│   │   └── VipDiscountUtil.java          # VIP折扣计算工具
│   ├── aspect/                           # AOP切面
│   │   ├── LogAspect.java                # 日志切面
│   │   └── TransactionAspect.java        # 事务切面
│   ├── listener/                         # 监听器
│   │   └── SessionListener.java          # Session监听器
│   └── exception/                        # 异常处理
│       ├── BusinessException.java        # 业务异常
│       └── GlobalExceptionHandler.java   # 全局异常处理
├── src/main/resources/
│   ├── application.yml                   # 配置文件
│   ├── static/                           # 静态资源
│   │   ├── css/
│   │   │   └── bootstrap.min.css         # 补充
│   │   ├── js/
│   │   │   ├── axios.min.js
│   │   │   ├── jquery-3.6.4.min.js
│   │   │   └── bootstrap.bundle.min.js
│   │   └── img/
│   └── templates/                        # 模板文件
│       ├── customer/                     # 顾客管理页
│       │   ├── add.html
│       │   ├── edit.html
│       │   └── list.html
│       ├── dashboard/                    # 仪表板页（补充）
│       │   └── export.html
│       ├── error/                        # 错误页
│       │   ├── 404.html
│       │   └── 500.html
│       ├── fruit/                        # 水果管理页
│       │   ├── add.html
│       │   ├── edit.html
│       │   └── list.html
│       ├── order/                        # 订单管理页
│       │   ├── add.html
│       │   └── list.html
│       ├── supplier/                     # 供应商管理页
│       ├── login.html                    # 登录页（补充，通常在根目录）
│       └── index.html                    # 首页（补充，通常在根目录）
├── pom.xml                               # Maven配置
└── README.md                             # 项目说明文档
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
