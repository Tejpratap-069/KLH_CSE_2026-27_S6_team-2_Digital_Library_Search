#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT"
mkdir -p build/classes
find backend/src -name '*.java' | sort > build/sources.txt
javac -encoding UTF-8 -d build/classes @build/sources.txt
echo "Compiled production sources into build/classes"
