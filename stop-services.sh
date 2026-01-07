#!/bin/bash

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${YELLOW}Stopping all microservices...${NC}"

# Find and kill all Spring Boot services
pkill -f "discovery-service-1.0.0.jar" && echo -e "${GREEN}✓ Stopped Discovery Service${NC}"
pkill -f "auth-service-1.0.0.jar" && echo -e "${GREEN}✓ Stopped Auth Service${NC}"
pkill -f "product-service-1.0.0.jar" && echo -e "${GREEN}✓ Stopped Product Service${NC}"
pkill -f "stock-service-1.0.0.jar" && echo -e "${GREEN}✓ Stopped Stock Service${NC}"
pkill -f "gateway-service-1.0.0.jar" && echo -e "${GREEN}✓ Stopped Gateway Service${NC}"
pkill -f "agent-ia-service-1.0.0.jar" && echo -e "${GREEN}✓ Stopped Agent IA Service${NC}"

sleep 2

echo -e "\n${GREEN}All services stopped!${NC}"

# Check if any services are still running
if pgrep -f "service-1.0.0.jar" > /dev/null; then
    echo -e "${RED}Warning: Some services may still be running:${NC}"
    ps aux | grep "service-1.0.0.jar" | grep -v grep
else
    echo -e "${GREEN}All services cleanly stopped.${NC}"
fi
