#!/bin/bash
# ============================================================
# Shift Transport System — Quick Start Script
# Double-click this file to start the application
# Then open: http://localhost:8080/auth/login
# ============================================================

PROJECT_DIR="/Users/shimonesterkin/Documents/Claude/Projects/Transportation system project"
cd "$PROJECT_DIR" || { echo "ERROR: project folder not found at $PROJECT_DIR"; read -p "Press Enter..."; exit 1; }

echo "================================================"
echo "  Shift Transport System — Starting..."
echo "================================================"
echo ""

# Find Maven in common locations
find_mvn() {
    command -v mvn 2>/dev/null && return
    [ -f /opt/homebrew/bin/mvn ]  && echo /opt/homebrew/bin/mvn  && return
    [ -f /usr/local/bin/mvn ]     && echo /usr/local/bin/mvn     && return
    find ~/Library/Application\ Support/JetBrains -name mvn -type f 2>/dev/null | head -1
}

MVN=$(find_mvn)

if [ -z "$MVN" ]; then
    echo "ERROR: Maven not found."
    echo "Please open IntelliJ IDEA and run ShiftTransportApplication from there."
    read -p "Press Enter to close..."
    exit 1
fi

echo "Using Maven: $MVN"
echo ""
echo "Once started, open Chrome and go to:"
echo "  http://localhost:8080/auth/login"
echo ""
echo "================================================"

"$MVN" spring-boot:run
