#!/bin/bash

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}Starting Microservices in Order${NC}"
echo -e "${GREEN}========================================${NC}"

# Function to wait for service to be ready
wait_for_service() {
    local service_name=$1
    local port=$2
    local max_wait=60
    local count=0
    
    echo -e "${YELLOW}Waiting for $service_name on port $port...${NC}"
    while ! nc -z localhost $port 2>/dev/null; do
        sleep 1
        count=$((count + 1))
        if [ $count -ge $max_wait ]; then
            echo -e "${RED}Timeout waiting for $service_name${NC}"
            return 1
        fi
    done
    echo -e "${GREEN}✓ $service_name is ready!${NC}"
    return 0
}

# Start Discovery Service (Eureka)
echo -e "\n${YELLOW}[1/6] Starting Discovery Service...${NC}"
cd discovery-service
java -jar target/discovery-service-1.0.0.jar > ../logs/discovery.log 2>&1 &
DISCOVERY_PID=$!
echo "Discovery Service PID: $DISCOVERY_PID"
cd ..
wait_for_service "Discovery Service" 8761

# Start Auth Service
echo -e "\n${YELLOW}[2/6] Starting Auth Service...${NC}"
cd auth-service
java -jar target/auth-service-1.0.0.jar > ../logs/auth.log 2>&1 &
AUTH_PID=$!
echo "Auth Service PID: $AUTH_PID"
cd ..
wait_for_service "Auth Service" 8080

# Start Product Service (MCP Server)
echo -e "\n${YELLOW}[3/6] Starting Product Service (MCP Server)...${NC}"
cd product-service
java -jar target/product-service-1.0.0.jar > ../logs/product.log 2>&1 &
PRODUCT_PID=$!
echo "Product Service PID: $PRODUCT_PID"
cd ..
wait_for_service "Product Service" 9091

# Start Stock Service (MCP Server)
echo -e "\n${YELLOW}[4/6] Starting Stock Service (MCP Server)...${NC}"
cd stock-service
java -jar target/stock-service-1.0.0.jar > ../logs/stock.log 2>&1 &
STOCK_PID=$!
echo "Stock Service PID: $STOCK_PID"
cd ..
wait_for_service "Stock Service" 9092

# Start Gateway Service
echo -e "\n${YELLOW}[5/6] Starting Gateway Service...${NC}"
cd gateway-service
java -jar target/gateway-service-1.0.0.jar > ../logs/gateway.log 2>&1 &
GATEWAY_PID=$!
echo "Gateway Service PID: $GATEWAY_PID"
cd ..
wait_for_service "Gateway Service" 8888

# Start Agent IA Service (MCP Client with Ollama)
echo -e "\n${YELLOW}[6/6] Starting Agent IA Service (MCP Client)...${NC}"
cd agent-ia-service
java -jar target/agent-ia-service-1.0.0.jar > ../logs/agent-ia.log 2>&1 &
AGENT_PID=$!
echo "Agent IA Service PID: $AGENT_PID"
cd ..
wait_for_service "Agent IA Service" 8089

echo -e "\n${GREEN}========================================${NC}"
echo -e "${GREEN}All services started successfully!${NC}"
echo -e "${GREEN}========================================${NC}"
echo -e "\nProcess IDs:"
echo "  Discovery Service: $DISCOVERY_PID"
echo "  Auth Service:      $AUTH_PID"
echo "  Product Service:   $PRODUCT_PID"
echo "  Stock Service:     $STOCK_PID"
echo "  Gateway Service:   $GATEWAY_PID"
echo "  Agent IA Service:  $AGENT_PID"

echo -e "\n${YELLOW}Service URLs:${NC}"
echo "  Eureka Dashboard:  http://localhost:8761"
echo "  Gateway:           http://localhost:8888"
echo "  Auth Service:      http://localhost:8080"
echo "  Product Service:   http://localhost:9091"
echo "  Stock Service:     http://localhost:9092"
echo "  Agent IA Service:  http://localhost:8089"

echo -e "\n${YELLOW}To stop all services:${NC}"
echo "  kill $DISCOVERY_PID $AUTH_PID $PRODUCT_PID $STOCK_PID $GATEWAY_PID $AGENT_PID"

echo -e "\n${YELLOW}To view logs:${NC}"
echo "  tail -f logs/discovery.log"
echo "  tail -f logs/auth.log"
echo "  tail -f logs/product.log"
echo "  tail -f logs/stock.log"
echo "  tail -f logs/gateway.log"
echo "  tail -f logs/agent-ia.log"
