#!/bin/bash
# CURL Commands for Testing Microservices Project

echo "=================================="
echo "MICROSERVICES TESTING COMMANDS"
echo "=================================="
echo ""

# Test 1: Discovery Service
echo "TEST 1: Discovery Service (Eureka Registry)"
echo "Command:"
echo 'curl -s http://localhost:8761/eureka/apps | jq .'
echo ""
echo "Or with format:"
echo 'curl -s -H "Accept: application/json" http://localhost:8761/eureka/apps'
echo ""

# Test 2: Get Applications from Eureka
echo "TEST 2: Get all registered applications"
echo "Command:"
echo 'curl -s http://localhost:8761/eureka/apps/application | jq .'
echo ""

# Test 3: Get specific application
echo "TEST 3: Get Auth Service from Eureka"
echo "Command:"
echo 'curl -s http://localhost:8761/eureka/apps/AUTH-SERVICE | jq .'
echo ""

# Test 4: Gateway Service - List all services
echo "TEST 4: Gateway Service - Health Check (if actuator configured)"
echo "Command:"
echo 'curl -s http://localhost:8888/actuator/health'
echo ""

# Test 5: Auth Service - Health Check
echo "TEST 5: Auth Service - Health Check (if actuator configured)"
echo "Command:"
echo 'curl -s http://localhost:8080/actuator/health'
echo ""

# Test 6: Product Service - Health Check
echo "TEST 6: Product Service - Health Check (if actuator configured)"
echo "Command:"
echo 'curl -s http://localhost:9091/actuator/health'
echo ""

# Test 7: Using PowerShell (Windows) - Invoke-WebRequest
echo "TEST 7: PowerShell/Windows Alternative Commands"
echo ""
echo "# Discovery Service"
echo 'Invoke-WebRequest -Uri http://localhost:8761/eureka/apps -UseBasicParsing'
echo ""
echo "# Auth Service"
echo 'Invoke-WebRequest -Uri http://localhost:8080/ -UseBasicParsing'
echo ""
echo "# Product Service"
echo 'Invoke-WebRequest -Uri http://localhost:9091/ -UseBasicParsing'
echo ""
echo "# Gateway Service"
echo 'Invoke-WebRequest -Uri http://localhost:8888/ -UseBasicParsing'
echo ""

# Additional useful commands
echo "ADDITIONAL COMMANDS"
echo "=================================="
echo ""

echo "# Register a new service instance"
echo 'curl -X POST http://localhost:8761/eureka/apps/MY-SERVICE/localhost:my-service:8080 \\'
echo '  -H "Content-Type: application/json" \\'
echo '  -d @instance.json'
echo ""

echo "# Deregister a service"
echo 'curl -X DELETE http://localhost:8761/eureka/apps/SERVICE-NAME/INSTANCE-ID'
echo ""

echo "# Heartbeat to keep instance alive"
echo 'curl -X PUT http://localhost:8761/eureka/apps/SERVICE-NAME/INSTANCE-ID'
echo ""

echo "# Monitor response times"
echo 'time curl -s http://localhost:8761/eureka/apps > /dev/null'
echo ""
