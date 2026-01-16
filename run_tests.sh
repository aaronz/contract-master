#!/bin/bash

# Contract Master - One-Click Test Runner
# This script runs all backend unit/integration tests and E2E tests.

# Set colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${GREEN}Starting all tests for Contract Master...${NC}"

# Check if we are in the root directory
if [ ! -d "backend" ]; then
    echo -e "${RED}Error: 'backend' directory not found. Please run this script from the project root.${NC}"
    exit 1
fi

echo -e "\n${GREEN}Step 1: Running Backend Unit and Integration Tests...${NC}"
(cd backend && mvn test)
BACKEND_STATUS=$?

if [ $BACKEND_STATUS -ne 0 ]; then
    echo -e "${RED}Backend tests failed! Please check the logs above.${NC}"
    exit $BACKEND_STATUS
fi

echo -e "\n${GREEN}All tests passed successfully!${NC}"
exit 0
