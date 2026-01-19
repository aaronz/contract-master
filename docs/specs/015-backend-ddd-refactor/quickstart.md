# Quickstart Guide: Backend DDD Refactor Development

本指南提供了快速设置开发环境并开始使用重构后的后端代码库的方法，该代码库现在遵循领域驱动设计（DDD）原则。

## 1. 前提条件

*   Java Development Kit (JDK) 17 或更高版本
*   Apache Maven 3.8.x 或更高版本
*   Docker 和 Docker Compose (用于本地数据库和消息队列设置)
*   Git 客户端
*   您偏好的IDE (例如，IntelliJ IDEA, VS Code)

## 2. 克隆代码仓库

```bash
git clone <repository-url>
cd contract-master
git checkout 001-backend-ddd-refactor
```

## 3. 本地环境设置 (Docker Compose)

项目使用 Docker Compose 管理 PostgreSQL (数据库), Redis (缓存) 和 Kafka (消息代理) 的本地实例。

1.  导航到项目根目录。
2.  启动服务:
    ```bash
    docker-compose up -d postgres redis kafka
    ```
    这将在分离模式下启动 PostgreSQL, Redis 和 Kafka。您可以使用 `docker-compose ps` 验证它们的状态。

## 4. 构建并运行后端应用

1.  导航到 `backend/` 目录:
    ```bash
    cd backend
    ```
2.  使用 Maven 构建项目:
    ```bash
    mvn clean install
    ```
3.  运行 Spring Boot 应用:
    ```bash
    mvn spring-boot:run
    ```
    应用程序应该启动并可访问 (默认端口通常是8080)。

## 5. 理解新的DDD结构

后端应用程序 (`backend/src/main/java/com/contractmaster/backend/`) 现在被划分为不同的层:

*   **`application/`**: 包含应用服务和用例。这一层协调领域对象以满足用户请求。
*   **`domain/`**: 应用程序的核心，包含核心业务逻辑。您会在这里找到:
    *   `model/`: 领域实体、值对象和聚合根 (例如，`Contract`, `Tenant`)。
    *   `service/`: 领域服务 (不适合放在实体/值对象中的业务逻辑)。
    *   `repository/`: 数据持久化接口 (在 `infrastructure` 中实现)。
*   **`infrastructure/`**: 处理技术问题，如数据库访问、外部服务集成和消息代理交互 (例如，仓储的 `spring-data-jpa` 实现，Kafka 生产者/消费者)。
*   **`interfaces/`**: 表示/外部层，包含REST控制器、DTO和API定义。

**快速掌握一个功能:**

*   从 `interfaces/` (例如，一个REST控制器) 开始，查看如何接收外部请求。
*   跟随调用到 `application/` 层，了解具体的用例。
*   `application/` 层将与 `domain/` 层 (实体、服务、仓储) 交互以执行业务逻辑。
*   最后，`domain/repository/` 接口在 `infrastructure/` 层中实现，用于持久化/检索数据。

## 6. 运行测试

要运行后端的所有单元测试和集成测试:

```bash
cd backend
mvn test
```

## 7. 清理

要停止并移除 Docker Compose 服务:

```bash
docker-compose down
```
