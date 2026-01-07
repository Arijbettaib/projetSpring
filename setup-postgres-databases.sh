#!/bin/bash

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}PostgreSQL Database Setup${NC}"
echo -e "${GREEN}========================================${NC}"

# PostgreSQL credentials
PGUSER=postgres
PGPASSWORD=postgres

export PGPASSWORD=$PGPASSWORD

echo -e "\n${YELLOW}Checking PostgreSQL connection...${NC}"
if ! psql -U $PGUSER -h localhost -c "SELECT version();" > /dev/null 2>&1; then
    echo -e "${RED}✗ Cannot connect to PostgreSQL. Please ensure PostgreSQL is running.${NC}"
    echo -e "${YELLOW}Start PostgreSQL with: brew services start postgresql@14${NC}"
    exit 1
fi
echo -e "${GREEN}✓ PostgreSQL is running${NC}"

# Create databases
echo -e "\n${YELLOW}Creating databases...${NC}"

# Drop databases if they exist (for clean setup)
echo -e "${YELLOW}Dropping existing databases if any...${NC}"
psql -U $PGUSER -h localhost -c "DROP DATABASE IF EXISTS auth_db;" 2>/dev/null
psql -U $PGUSER -h localhost -c "DROP DATABASE IF EXISTS product_db;" 2>/dev/null
psql -U $PGUSER -h localhost -c "DROP DATABASE IF EXISTS stock_db;" 2>/dev/null

# Create new databases
echo -e "${YELLOW}Creating auth_db...${NC}"
psql -U $PGUSER -h localhost -c "CREATE DATABASE auth_db;"
if [ $? -eq 0 ]; then
    echo -e "${GREEN}✓ auth_db created${NC}"
else
    echo -e "${RED}✗ Failed to create auth_db${NC}"
fi

echo -e "${YELLOW}Creating product_db...${NC}"
psql -U $PGUSER -h localhost -c "CREATE DATABASE product_db;"
if [ $? -eq 0 ]; then
    echo -e "${GREEN}✓ product_db created${NC}"
else
    echo -e "${RED}✗ Failed to create product_db${NC}"
fi

echo -e "${YELLOW}Creating stock_db...${NC}"
psql -U $PGUSER -h localhost -c "CREATE DATABASE stock_db;"
if [ $? -eq 0 ]; then
    echo -e "${GREEN}✓ stock_db created${NC}"
else
    echo -e "${RED}✗ Failed to create stock_db${NC}"
fi

# List all databases
echo -e "\n${YELLOW}Current databases:${NC}"
psql -U $PGUSER -h localhost -c "\l" | grep -E "auth_db|product_db|stock_db"

echo -e "\n${GREEN}========================================${NC}"
echo -e "${GREEN}Database setup completed!${NC}"
echo -e "${GREEN}========================================${NC}"

echo -e "\n${YELLOW}Database Details:${NC}"
echo "  Host:     localhost"
echo "  Port:     5432"
echo "  User:     postgres"
echo "  Password: postgres"
echo "  Databases:"
echo "    - auth_db"
echo "    - product_db"
echo "    - stock_db"

unset PGPASSWORD
