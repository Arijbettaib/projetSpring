# 🚀 Microservices Startup Guide

## ✅ Build Status
**BUILD SUCCESS** - All 7 microservices compiled successfully!

```
✅ discovery-service   - SUCCESS [1.274s]
✅ gateway-service     - SUCCESS [0.557s]  
✅ auth-service        - SUCCESS [0.856s]
✅ product-service     - SUCCESS [0.778s] (MCP Server)
✅ stock-service       - SUCCESS [0.884s] (MCP Server)
✅ agent-ia-service    - SUCCESS [0.795s] (MCP Client + Ollama)
```

## 📋 Prerequisites

### 1. MySQL Database Setup
Create the required databases:

```bash
mysql -u root -p < setup-databases.sql
```

Or manually:
```sql
CREATE DATABASE IF NOT EXISTS auth2;
CREATE DATABASE IF NOT EXISTS product_db;
CREATE DATABASE IF NOT EXISTS stock_db;
```

**Database Credentials** (default):
- Username: `root`
- Password: *(empty - change in application.properties if different)*

### 2. Install Ollama (for AI Agent)
```bash
# macOS
brew install ollama

# Start Ollama service
ollama serve

# Pull the llama3.2 model (in a new terminal)
ollama pull llama3.2
```

Verify Ollama is running:
```bash
curl http://localhost:11434/api/tags
```

## 🏃 Starting the Services

### Option 1: Automated Startup (Recommended)
```bash
./start-services.sh
```

This will start all services in the correct order:
1. Discovery Service (Eureka) - Port 8761
2. Auth Service - Port 8080
3. Product Service (MCP Server) - Port 9091
4. Stock Service (MCP Server) - Port 9092
5. Gateway Service - Port 8888
6. Agent IA Service (MCP Client) - Port 8081

### Option 2: Manual Startup
Start each service individually in separate terminals:

```bash
# Terminal 1 - Discovery Service
cd discovery-service && java -jar target/discovery-service-1.0.0.jar

# Terminal 2 - Auth Service
cd auth-service && java -jar target/auth-service-1.0.0.jar

# Terminal 3 - Product Service
cd product-service && java -jar target/product-service-1.0.0.jar

# Terminal 4 - Stock Service
cd stock-service && java -jar target/stock-service-1.0.0.jar

# Terminal 5 - Gateway Service
cd gateway-service && java -jar target/gateway-service-1.0.0.jar

# Terminal 6 - Agent IA Service
cd agent-ia-service && java -jar target/agent-ia-service-1.0.0.jar
```

## 🛑 Stopping the Services

```bash
./stop-services.sh
```

Or manually find and kill the processes:
```bash
pkill -f "service-1.0.0.jar"
```

## 📊 Service URLs

| Service | URL | Description |
|---------|-----|-------------|
| **Eureka Dashboard** | http://localhost:8761 | Service registry UI |
| **API Gateway** | http://localhost:8888 | Main entry point |
| **Auth Service** | http://localhost:8080 | Authentication & JWT |
| **Product Service** | http://localhost:9091 | Product CRUD + MCP Server |
| **Stock Service** | http://localhost:9092 | Stock management + MCP Server |
| **Agent IA Service** | http://localhost:8081 | AI Agent with Ollama + MCP Client |

## 🧪 Testing the Services

### 1. Check Service Registration
Visit Eureka Dashboard: http://localhost:8761

All services should show as "UP".

### 2. Get Authentication Token
```bash
# Register a user
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123",
    "email": "test@example.com"
  }'

# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }'
```

Save the JWT token from the response.

### 3. Test Product Service (MCP Server)
```bash
# Create a product
curl -X POST http://localhost:9091/api/products \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "name": "Laptop",
    "price": 999.99,
    "description": "High-performance laptop"
  }'

# Get all products
curl http://localhost:9091/api/products \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 4. Test Stock Service (MCP Server)
```bash
# Create stock entry
curl -X POST http://localhost:9092/api/stocks \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "productId": 1,
    "quantity": 50
  }'

# Get stock by product ID
curl http://localhost:9092/api/stocks/product/1 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 5. Test MCP Integration
Check MCP endpoints:
```bash
# Product MCP Server
curl http://localhost:9091/actuator/mcp

# Stock MCP Server
curl http://localhost:9092/actuator/mcp
```

### 6. Test AI Agent (MCP Client)
```bash
# Chat with AI Agent that uses MCP tools
curl -X POST http://localhost:8081/api/agent/chat \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "message": "Show me all products",
    "userId": "testuser"
  }'

# The AI agent will use MCP tools to fetch products from product-service
```

## 📁 View Logs

```bash
# Real-time logs for all services
tail -f logs/discovery.log
tail -f logs/auth.log
tail -f logs/product.log
tail -f logs/stock.log
tail -f logs/gateway.log
tail -f logs/agent-ia.log
```

## 🔧 Troubleshooting

### Database Connection Errors
- Check MySQL is running: `mysql.server status`
- Verify databases exist: `mysql -u root -p -e "SHOW DATABASES;"`
- Check credentials in `application.properties`/`application.yml`

### Service Not Registering with Eureka
- Wait 30-60 seconds for registration
- Check Eureka is running: http://localhost:8761
- Verify service logs for errors

### Ollama Not Connected
- Start Ollama: `ollama serve`
- Pull model: `ollama pull llama3.2`
- Check Ollama API: `curl http://localhost:11434/api/tags`

### Port Already in Use
```bash
# Find process using a port
lsof -i :8761  # Replace with your port

# Kill the process
kill -9 <PID>
```

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────┐
│                   Discovery Service                      │
│                   (Eureka - 8761)                        │
└─────────────────────────────────────────────────────────┘
                          ▲
                          │
                ┌─────────┴─────────┐
                │                   │
┌───────────────▼────┐   ┌─────────▼────────────┐
│  Gateway Service   │   │   Auth Service       │
│   (Port 8888)      │   │   (Port 8080)        │
└───────────────┬────┘   └──────────────────────┘
                │
        ┌───────┴───────┐
        │               │
┌───────▼──────┐  ┌────▼──────────┐
│Product Service│  │Stock Service  │
│(MCP Server)   │  │(MCP Server)   │
│Port 9091      │  │Port 9092      │
└───────┬───────┘  └────┬──────────┘
        │               │
        │    MCP SSE    │
        └───────┬───────┘
                │
    ┌───────────▼──────────────┐
    │  Agent IA Service        │
    │  (MCP Client + Ollama)   │
    │  Port 8081               │
    └──────────────────────────┘
```

## 🎯 MCP Integration

**MCP Servers** (Expose tools):
- `product-service` - Product CRUD operations as MCP tools
- `stock-service` - Stock management operations as MCP tools

**MCP Client**:
- `agent-ia-service` - Consumes MCP tools from both servers
- Uses Ollama (llama3.2) for AI responses
- Automatically discovers and uses tools via SSE transport

## 📚 Next Steps

1. ✅ **Build completed** - All services compiled
2. 🔄 **Setup databases** - Run `setup-databases.sql`
3. 🤖 **Install Ollama** - Required for AI agent
4. 🚀 **Start services** - Run `./start-services.sh`
5. 🧪 **Test endpoints** - Use curl commands above
6. 🎨 **Build frontend** - Optional UI integration

## 🔐 Security Notes

- JWT secret is configured in `auth-service/application.yml`
- Default token expiration: 24 hours (86400000 ms)
- Change MySQL root password in production
- Update JWT secret for production deployments
