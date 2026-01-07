# PostgreSQL Microservices - Quick Start Guide

## ✅ Build Status: SUCCESS
All 7 microservices compiled successfully with PostgreSQL!

## 📋 Prerequisites
- Java 17+
- Maven 3.6+
- **PostgreSQL** (running on localhost:5432)
- Ollama with llama3.2 model (for AI agent)

## 🗄️ PostgreSQL Configuration

### Database Setup
```bash
# Create databases
./setup-postgres-databases.sh
```

This creates three databases:
- **auth_db** - Authentication and user management
- **product_db** - Product catalog
- **stock_db** - Stock/inventory management

### Credentials
- **Host**: localhost
- **Port**: 5432
- **User**: postgres
- **Password**: postgres

## 🏗️ Build Project
```bash
mvn clean install -DskipTests
```

## 🚀 Starting Services

### Option 1: Automated Startup (Recommended)
```bash
./start-services.sh
```

This script will:
1. Start Discovery Service (Eureka) on port 8761
2. Start Auth Service on port 8080
3. Start Product Service (MCP Server) on port 9091
4. Start Stock Service (MCP Server) on port 9092
5. Start Gateway Service on port 8888
6. Start Agent IA Service (MCP Client) on port 8089

### Option 2: Manual Startup
Start services in this order:

```bash
# 1. Discovery Service
cd discovery-service
java -jar target/discovery-service-1.0.0.jar

# 2. Auth Service
cd auth-service
java -jar target/auth-service-1.0.0.jar

# 3. Product Service (MCP Server)
cd product-service
java -jar target/product-service-1.0.0.jar

# 4. Stock Service (MCP Server)
cd stock-service
java -jar target/stock-service-1.0.0.jar

# 5. Gateway Service
cd gateway-service
java -jar target/gateway-service-1.0.0.jar

# 6. Agent IA Service (MCP Client)
cd agent-ia-service
java -jar target/agent-ia-service-1.0.0.jar
```

## 🔗 Service URLs

| Service | URL | Description |
|---------|-----|-------------|
| Eureka Dashboard | http://localhost:8761 | Service registry |
| Gateway | http://localhost:8888 | API Gateway |
| Auth Service | http://localhost:8080 | Authentication |
| Product Service | http://localhost:9091 | Product CRUD + MCP |
| Stock Service | http://localhost:9092 | Stock CRUD + MCP |
| Agent IA | http://localhost:8089 | AI Agent with Ollama |

## 🧪 Testing the System

### 1. Check Eureka Dashboard
Visit http://localhost:8761 to verify all services are registered.

### 2. Test Authentication
```bash
# Register a new user
curl -X POST http://localhost:8888/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123","email":"admin@test.com"}'

# Login to get JWT token
curl -X POST http://localhost:8888/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

### 3. Test Product Service (MCP Server)
```bash
# Create a product
curl -X POST http://localhost:8888/api/products \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{"name":"Laptop","description":"Dell XPS 15","price":1500.00}'

# Get all products
curl http://localhost:8888/api/products \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

# Check MCP endpoint
curl http://localhost:9091/actuator/mcp
```

### 4. Test Stock Service (MCP Server)
```bash
# Create stock entry
curl -X POST http://localhost:8888/api/stocks \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{"productId":1,"quantity":100}'

# Get stock by product
curl http://localhost:8888/api/stocks/product/1 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

# Check MCP endpoint
curl http://localhost:9092/actuator/mcp
```

### 5. Test AI Agent (MCP Client)
```bash
# Chat with AI agent (connected to Product & Stock MCP servers)
curl -X POST http://localhost:8089/api/agent/chat \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{"message":"List all products","userId":"admin"}'

# Stream response
curl -X POST http://localhost:8089/api/agent/chat/stream \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{"message":"What products are available?","userId":"admin"}'
```

## 📊 MCP Architecture

The system uses Spring AI Model Context Protocol (MCP):

- **MCP Servers**: Product Service & Stock Service expose their operations as tools
- **MCP Client**: Agent IA Service connects to both MCP servers via SSE transport
- **AI Model**: Ollama with llama3.2 processes requests and calls MCP tools

```
┌─────────────────┐
│  Agent IA       │
│  (MCP Client)   │
│  + Ollama       │
└────────┬────────┘
         │
         ├──────────┐
         │          │
    ┌────▼───┐  ┌──▼─────┐
    │Product │  │ Stock  │
    │Service │  │Service │
    │(MCP)   │  │(MCP)   │
    └────┬───┘  └──┬─────┘
         │         │
      ┌──▼─────────▼──┐
      │  PostgreSQL   │
      │  3 Databases  │
      └───────────────┘
```

## 🛠️ Troubleshooting

### PostgreSQL Connection Issues
```bash
# Check if PostgreSQL is running
psql -U postgres -c "SELECT version();"

# On macOS with Homebrew
brew services start postgresql@14
```

### Port Conflicts
If ports are already in use, update `application.properties` or `application.yml` in each service.

### Ollama Not Running
```bash
# Install Ollama
brew install ollama  # macOS

# Start Ollama
ollama serve

# Pull llama3.2 model
ollama pull llama3.2
```

### View Logs
```bash
tail -f logs/discovery.log
tail -f logs/auth.log
tail -f logs/product.log
tail -f logs/stock.log
tail -f logs/gateway.log
tail -f logs/agent-ia.log
```

## 📝 Configuration Changes (MySQL → PostgreSQL)

### Dependencies Updated
- ✅ Replaced `mysql-connector-j` with `postgresql` in all service POMs
- ✅ Updated JDBC URLs to `jdbc:postgresql://localhost:5432/`
- ✅ Changed Hibernate dialect to `PostgreSQLDialect`
- ✅ Updated database driver to `org.postgresql.Driver`

### Database Names
- `auth2` → `auth_db`
- `product_db` → `product_db`
- `stock_db` → `stock_db`

## 🔐 Security Notes
⚠️ **Default credentials are for development only!**

In production:
- Change PostgreSQL password
- Use environment variables for secrets
- Rotate JWT secret key
- Enable SSL/TLS for database connections

## 📚 Additional Resources
- [Spring AI Documentation](https://docs.spring.io/spring-ai/reference/)
- [MCP Protocol Spec](https://spec.modelcontextprotocol.io/)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [Ollama Documentation](https://ollama.ai/docs)
