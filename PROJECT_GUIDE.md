# 🚀 Spring Microservices Project - Complete Guide

## 📋 Project Overview

This is a microservices-based application built with **Spring Boot 3.3.5**, **Spring Cloud 2023.0.3**, and **Spring AI 1.1.1** with MCP (Model Context Protocol) support. The system includes:

| Service | Port | Description |
|---------|------|-------------|
| **discovery-service** | 8761 | Eureka Server for service registry |
| **gateway-service** | 8888 | API Gateway with JWT authentication |
| **auth-service** | 8080 | Authentication service (JWT generation) |
| **product-service** | 9091 | Product management (CRUD) + MCP Server |
| **stock-service** | 9092 | Stock/Inventory management + MCP Server |
| **agent-ia-service** | 8089 | AI Agent with Ollama + MCP Client |

---

## 🔧 Configuration Applied

### Version Updates
- **Spring Boot**: 3.3.5 (latest stable)
- **Spring Cloud**: 2023.0.3
- **Spring AI**: 1.1.1 (with MCP support)

### MCP Architecture
```
┌─────────────────┐     ┌──────────────────┐     ┌─────────────────┐
│   Ollama LLM    │────▶│  agent-ia-service │────▶│  MCP Clients    │
│  (llama3.2)     │     │   (MCP Client)    │     │                 │
└─────────────────┘     └──────────────────┘     └────────┬────────┘
                                                          │
                              ┌───────────────────────────┼───────────────────────────┐
                              │                           │                           │
                              ▼                           ▼                           ▼
                    ┌─────────────────┐         ┌─────────────────┐         ┌─────────────────┐
                    │ product-service │         │  stock-service  │         │   Other MCP     │
                    │  (MCP Server)   │         │  (MCP Server)   │         │    Servers      │
                    └─────────────────┘         └─────────────────┘         └─────────────────┘
```

---

## 📦 Prerequisites

Before running the project, ensure you have:

1. **Java 17+** installed
   ```bash
   java -version
   ```

2. **Maven 3.8+** installed
   ```bash
   mvn -version
   ```

3. **MySQL 8.0+** running on `localhost:3306`
   ```bash
   mysql -u root -p -e "SELECT VERSION();"
   ```

4. **MySQL Databases** created:
   ```sql
   CREATE DATABASE IF NOT EXISTS auth2;
   CREATE DATABASE IF NOT EXISTS product_db;
   CREATE DATABASE IF NOT EXISTS stock_db;
   ```

5. **Ollama** (for AI Agent - Optional but recommended)
   ```bash
   # Install Ollama
   curl -fsSL https://ollama.com/install.sh | sh
   
   # Pull the model
   ollama pull llama3.2
   
   # Start Ollama server (usually auto-starts)
   ollama serve
   ```

---

## 🗄️ Database Setup

Connect to MySQL and run:

```sql
-- Create databases
CREATE DATABASE IF NOT EXISTS auth2;
CREATE DATABASE IF NOT EXISTS product_db;
CREATE DATABASE IF NOT EXISTS stock_db;

-- Verify
SHOW DATABASES;
```

### Default MySQL Configuration
All services are configured to use:
- **Username:** `root`
- **Password:** (empty)
- **Host:** `localhost:3306`

If your MySQL has a different password, update the `application.properties` or `application.yml` files in:
- `auth-service/src/main/resources/application.yml`
- `product-service/src/main/resources/application.properties`
- `stock-service/src/main/resources/application.properties`

---

## 🚀 Running the Project

### Step 1: Build All Services
From the root project directory:

```bash
cd /Users/amineouhiba/Desktop/arijBe/projetSpring
mvn clean install -DskipTests
```

### Step 2: Start Services (In Order!)

**⚠️ IMPORTANT:** Services must be started in this exact order:

#### Terminal 1 - Discovery Service (Eureka)
```bash
cd discovery-service
mvn spring-boot:run
```
Wait until you see: `Started DiscoveryServiceApplication`

Verify: Open http://localhost:8761 - You should see the Eureka dashboard.

#### Terminal 2 - Auth Service
```bash
cd auth-service
mvn spring-boot:run
```
Wait until you see: `Started AuthServiceApplication`

#### Terminal 3 - Product Service
```bash
cd product-service
mvn spring-boot:run
```
Wait until you see: `Started ProductServiceApplication`

#### Terminal 4 - Stock Service
```bash
cd stock-service
mvn spring-boot:run
```
Wait until you see: `Started StockServiceApplication`

#### Terminal 5 - Gateway Service
```bash
cd gateway-service
mvn spring-boot:run
```
Wait until you see: `Started GatewayServiceApplication`

#### Terminal 6 - Agent IA Service (Optional)
```bash
cd agent-ia-service
mvn spring-boot:run
```
Wait until you see: `Started AgentIaServiceApplication`

---

## 🧪 Testing the Application

### 1. Check Eureka Dashboard
Open: http://localhost:8761

You should see all registered services:
- AUTH-SERVICE
- PRODUCT-SERVICE
- STOCK-SERVICE
- GATEWAY-SERVICE
- AGENT-IA-SERVICE

### 2. Create Test User (First Time Only)

The `DataInitializer` in auth-service is commented out. To create a test user, you need to either:

**Option A:** Uncomment the `@Bean` annotations in `auth-service/src/main/java/ma/ensa/authservice/config/DataInitializer.java`

**Option B:** Insert manually via MySQL:
```sql
USE auth2;

-- Create role
INSERT INTO app_role (name) VALUES ('USER') ON DUPLICATE KEY UPDATE name=name;
INSERT INTO app_role (name) VALUES ('ADMIN') ON DUPLICATE KEY UPDATE name=name;

-- Create user (password: 1234 encoded with BCrypt)
INSERT INTO users (username, password) VALUES 
('user', '$2a$10$N9qo8uLOickgx2ZMRZoMye.IjqQBEl.fOtaM2mJRzJGYq4iRjvhzS');

-- Link user to role
INSERT INTO user_roles (user_id, role_id) 
SELECT u.id, r.id FROM users u, app_role r WHERE u.username='user' AND r.name='USER';
```

### 3. Get JWT Token

```bash
curl -X POST http://localhost:8888/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "user", "password": "1234"}'
```

Expected response:
```json
{"token": "eyJhbGciOiJIUzI1NiJ9..."}
```

**Save this token** for subsequent requests.

### 4. Test Product Service (via Gateway)

#### Create a Product
```bash
TOKEN="<paste_your_token_here>"

curl -X POST http://localhost:8888/api/products \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"name": "Laptop", "price": 999.99, "quantity": 10}'
```

#### Get All Products
```bash
curl -X GET http://localhost:8888/api/products \
  -H "Authorization: Bearer $TOKEN"
```

#### Get Product by ID
```bash
curl -X GET http://localhost:8888/api/products/1 \
  -H "Authorization: Bearer $TOKEN"
```

### 5. Test Stock Service (via Gateway)

#### Create Stock
```bash
curl -X POST http://localhost:8888/api/stocks \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"productId": 1, "quantity": 100}'
```

#### Get Stock by Product ID
```bash
curl -X GET http://localhost:8888/api/stocks/1 \
  -H "Authorization: Bearer $TOKEN"
```

### 6. Test Agent IA Service (via Gateway)

```bash
# Chat with the AI Agent
curl -X POST http://localhost:8888/api/agent/chat \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"message": "List all products"}'

# Get available tools
curl -X GET http://localhost:8888/api/agent/tools \
  -H "Authorization: Bearer $TOKEN"

# Direct chat endpoint
curl -X POST http://localhost:8888/chat \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '"Create a new product called Laptop with price 999.99"'
```

### 7. Test MCP Endpoints Directly (Optional)

```bash
# Product Service MCP
curl http://localhost:9091/actuator/health

# Stock Service MCP
curl http://localhost:9092/actuator/health
```

---

## 🔐 Security Notes

### JWT Configuration
Both `auth-service` and `gateway-service` use the same JWT secret:
```
jwt.secret=agent-ia-super-secret-key-2026-very-secure
```

**⚠️ For production:** Change this secret and use environment variables!

### Protected vs Public Endpoints
- `/auth/**` - **Public** (no JWT required)
- `/api/products/**` - **Protected** (JWT required)
- `/api/stocks/**` - **Protected** (JWT required)
- `/api/agent/**` - **Protected** (JWT required)

---

## 📁 Project Structure

```
projetSpring/
├── pom.xml                      # Parent POM (aggregator)
├── discovery-service/           # Eureka Server (Port: 8761)
├── gateway-service/             # API Gateway (Port: 8888)
├── auth-service/                # Authentication (Port: 8080)
├── product-service/             # Products CRUD (Port: 9091)
├── stock-service/               # Stock Management (Port: 9092)
└── agent-ia-service/            # AI Agent (Port: 8089)
```

---

## ⚠️ Known Limitations

1. **Ollama Required**: The AI Agent requires Ollama running locally with `llama3.2` model for full functionality.

2. **MCP Property Names**: Some Spring AI MCP properties may show IDE warnings but work at runtime.

3. **DataInitializer**: The user initialization beans are commented out. Uncomment them or manually insert test data.

---

## 🔄 Quick Commands Reference

```bash
# Build all
mvn clean install -DskipTests

# Run tests
mvn test

# Start single service
cd <service-name> && mvn spring-boot:run

# Package for deployment
mvn clean package -DskipTests

# Check service health
curl http://localhost:8761/actuator/health
curl http://localhost:8888/actuator/health
```

---

## 🐛 Troubleshooting

### Service not registering with Eureka
- Ensure discovery-service is running first
- Check `eureka.client.service-url.defaultZone` in application config

### JWT Token Invalid
- Ensure the same secret is used in auth-service and gateway-service
- Check token hasn't expired (default: 24 hours)

### MySQL Connection Failed
- Verify MySQL is running: `mysql -u root -p`
- Check database exists: `SHOW DATABASES;`
- Verify credentials in application.properties/yml

### Port Already in Use
```bash
# Find process using port
lsof -i :8761
# Kill process
kill -9 <PID>
```

---

## 📞 Support

For issues, check:
1. Service logs in the terminal
2. Eureka dashboard at http://localhost:8761
3. MySQL connectivity
