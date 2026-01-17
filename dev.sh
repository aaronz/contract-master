#!/bin/bash
set -e

ROOT_DIR="$(pwd)"
BACKEND_DIR="$ROOT_DIR/backend"
FRONTEND_DIR="$ROOT_DIR/frontend"

GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

log_info() { printf "${BLUE}>>> %s${NC}\n" "$1"; }
log_warn() { printf "${YELLOW}>>> %s${NC}\n" "$1"; }
log_success() { printf "${GREEN}>>> %s${NC}\n" "$1"; }
log_error() { printf "${RED}>>> %s${NC}\n" "$1"; }

if command -v mvn &> /dev/null; then
    MVN_CMD="mvn"
elif [ -f "$BACKEND_DIR/mvnw" ]; then
    MVN_CMD="./mvnw"
else
    MVN_CMD="mvn" 
fi

log_info "Starting Contract Master Development Environment..."

log_info "Resetting Docker environment..."
docker-compose down -v --remove-orphans
docker-compose up -d

cleanup() {
    printf "\n${YELLOW}>>> Shutting down processes...${NC}\n"
    kill $BACKEND_PID $FRONTEND_PID 2>/dev/null
    log_success "Done."
    exit
}
trap cleanup SIGINT SIGTERM

log_warn "Starting Backend (Spring Boot)..."
cd "$BACKEND_DIR"
if ! command -v $MVN_CMD &> /dev/null && [ "$MVN_CMD" == "mvn" ]; then
    log_error "Maven (mvn) not found. Please install Maven."
else
    $MVN_CMD spring-boot:run &
    BACKEND_PID=$!
fi

log_warn "Starting Frontend (Vue 3/Vite)..."
cd "$FRONTEND_DIR"
if [ ! -d "node_modules" ]; then
    log_info "node_modules not found, installing..."
    npm install
fi
npm run dev &
FRONTEND_PID=$!

log_success "Setup initiated!"
printf "${BLUE}Backend: http://localhost:8080${NC}\n"
printf "${BLUE}Frontend: http://localhost:5173 (or similar)${NC}\n"
log_warn "Press Ctrl+C to stop."

wait
