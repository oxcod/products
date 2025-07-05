# Products Application 工作记录

## 项目概述
这是一个使用Spring Boot + Kotlin构建的产品管理应用，主要功能是从外部API同步产品数据并提供Web界面进行查看和管理。

## 项目结构
```
products/
├── build.gradle.kts          # Gradle构建配置
├── src/main/
│   ├── java/xyz/pkq/products/
│   │   └── ProductsApplication.kt  # 主应用类
│   └── resources/
│       ├── application.yml         # 应用配置
│       └── db/migration/
│           └── Flyway V1__init.sql # 数据库迁移脚本
```

## 技术栈
- **后端框架**: Spring Boot 3.4.1 + Kotlin 2.1.0
- **数据库**: PostgreSQL + Flyway 迁移工具
- **前端**: HTMX + Thymeleaf + Shoelace UI组件
- **构建工具**: Gradle 与 Kotlin DSL
- **JVM版本**: 23 (最新版本)
- **其他**: JdbcClient, @Scheduled定时任务

## 当前状态

### 已完成
1. **项目基础结构**:
   - Spring Boot项目初始化
   - Gradle配置完成，包含所有必需依赖
   - PostgreSQL数据库连接配置（application.yml已配置）
   - Flyway数据库迁移配置

2. **数据库设计**:
   - `product` 表：存储产品基本信息（id, title, product_type）
   - `variant` 表：存储产品变体信息（id, product_id, title, price, available）
   - 采用外键关联的多表设计，而非JSONB

### 待实现
1. **核心功能模块**:
   - Product和Variant实体类
   - Repository层（使用JdbcClient）
   - Service层业务逻辑
   - 定时任务从famme.no同步产品数据
   - Web控制器和页面

2. **前端功能**:
   - 产品列表页面
   - 加载产品按钮
   - 产品表格展示（使用Shoelace组件）
   - 添加产品表单
   - HTMX无刷新更新

## 业务需求确认

### 核心功能需求
1. **产品数据同步**:
   - 定时任务从 https://famme.no/products.json 获取产品数据
   - 限制保存50个产品
   - 处理产品与变体的一对多关系

2. **Web界面功能**:
   - 显示"加载产品"按钮
   - 点击按钮后从数据库获取产品并展示在表格中
   - 提供添加新产品的表单
   - 所有操作使用HTMX实现无页面刷新

3. **数据字段选择**:
   - Product: id, title, product_type
   - Variant: id, product_id, title, price, available

## 技术需求确认

### 必须使用的技术
1. **数据访问**: JdbcClient（Spring新特性）
2. **定时任务**: @Scheduled注解，设置initialDelay=0
3. **前端框架**: HTMX + Thymeleaf服务端渲染
4. **UI组件**: Shoelace Web Components
5. **数据库迁移**: Flyway

### 开发工具要求
1. IntelliJ IDEA Ultimate（通过EAP计划）
2. 内置数据库客户端
3. 内置Git客户端
4. 内置HTTP客户端进行API测试

## 实施计划

### 第一阶段：数据模型和持久层（当前阶段）
- [ ] 创建Product和Variant实体类
- [ ] 实现ProductRepository和VariantRepository
- [ ] 编写基础的CRUD操作

### 第二阶段：数据同步服务
- [ ] 创建ProductSyncService
- [ ] 实现从famme.no获取JSON数据
- [ ] 解析JSON并转换为实体对象
- [ ] 实现批量保存逻辑（限制50个产品）
- [ ] 配置@Scheduled定时任务

### 第三阶段：Web界面开发
- [ ] 创建ProductController
- [ ] 设计Thymeleaf模板结构
- [ ] 集成Shoelace UI组件
- [ ] 实现HTMX交互逻辑

### 第四阶段：功能完善
- [ ] 添加产品表单功能
- [ ] 错误处理和验证
- [ ] 性能优化
- [ ] 编写HTTP客户端测试脚本

## 技术决策记录

### 为什么选择多表而非JSONB？
- 更好的查询性能
- 数据完整性保证
- 便于后续扩展和维护
- 符合关系型数据库设计原则

### 为什么选择这些字段？
- 保持简洁，只选择最关键的3-5个字段
- id: 唯一标识
- title: 产品/变体名称
- product_type: 产品分类
- price: 价格信息
- available: 库存状态

## 下一步行动
1. 创建实体类和Repository接口
2. 实现基础的数据访问层
3. 开始构建产品同步服务

---
*最后更新: 2025-07-05*