#!/bin/bash
set -e

GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

log_info() { printf "${BLUE}>>> %s${NC}\n" "$1"; }
log_success() { printf "${GREEN} [PASS] %s${NC}\n" "$1"; }
log_error() { printf "${RED} [FAIL] %s${NC}\n" "$1"; }
log_warn() { printf "${YELLOW} [WARN] %s${NC}\n" "$1"; }

check_java() {
    log_info "Checking Java version..."
    if command -v java &> /dev/null; then
        version=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | cut -d'.' -f1)
        if [ -z "$version" ]; then
             version=$(java -version 2>&1 | awk -F '"' '/openjdk version/ {print $2}' | cut -d'.' -f1)
        fi
        
        if [ "$version" -ge 17 ]; then
            log_success "Java version $version detected."
        else
            log_error "Java version 17 or higher is required. Found version $version."
            return 1
        fi
    else
        log_error "Java is not installed."
        return 1
    fi
}

check_node() {
    log_info "Checking Node.js version..."
    if command -v node &> /dev/null; then
        version=$(node -v | cut -d'v' -f2 | cut -d'.' -f1)
        if [ "$version" -ge 18 ]; then
            log_success "Node.js version $version detected."
        else
            log_error "Node.js version 18 or higher is required. Found version $version."
            return 1
        fi
    else
        log_error "Node.js is not installed."
        return 1
    fi
}

check_docker() {
    log_info "Checking Docker..."
    if command -v docker &> /dev/null; then
        docker_version=$(docker --version)
        log_success "Docker installed: $docker_version"
        
        if docker compose version &> /dev/null; then
            log_success "Docker Compose plugin detected."
        elif command -v docker-compose &> /dev/null; then
            log_success "docker-compose standalone detected."
        else
            log_warn "Docker Compose not found. It's required for local infrastructure."
        fi
    else
        log_error "Docker is not installed."
        return 1
    fi
}

check_maven() {
    log_info "Checking Maven..."
    if command -v mvn &> /dev/null; then
        mvn_version=$(mvn -version | head -n 1)
        log_success "Maven installed: $mvn_version"
    elif [ -f "./backend/mvnw" ]; then
        log_success "Maven Wrapper (mvnw) detected in backend directory."
    else
        log_warn "Maven not found in PATH and no wrapper detected."
    fi
}

echo "------------------------------------------"
echo "  Contract Master Environment Check"
echo "------------------------------------------"

EXIT_CODE=0

check_java || EXIT_CODE=1
check_node || EXIT_CODE=1
check_docker || EXIT_CODE=1
check_maven || true

echo "------------------------------------------"
if [ $EXIT_CODE -eq 0 ]; then
    log_success "Environment check passed! You are ready to develop."
else
    log_error "Environment check failed. Please address the issues above."
fi

exit $EXIT_CODE
