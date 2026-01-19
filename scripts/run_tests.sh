#!/bin/bash

GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m'

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
ROOT_DIR="$(cd "$SCRIPT_DIR/.." && pwd)"

echo -e "${GREEN}Starting all tests for Contract Master...${NC}"

if [ ! -d "$ROOT_DIR/backend" ]; then
    echo -e "${RED}Error: 'backend' directory not found at $ROOT_DIR/backend.${NC}"
    exit 1
fi

echo -e "\n${GREEN}Step 1: Running Backend Unit and Integration Tests...${NC}"
(cd "$ROOT_DIR/backend" && mvn test)
BACKEND_STATUS=$?

if [ $BACKEND_STATUS -ne 0 ]; then
    echo -e "${RED}Backend tests failed! Please check the logs above.${NC}"
    exit $BACKEND_STATUS
fi

echo -e "\n${GREEN}All tests passed successfully!${NC}"
exit 0
