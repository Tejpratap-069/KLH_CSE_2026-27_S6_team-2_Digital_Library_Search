#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "$0")" && pwd)"
cd "$ROOT"
./scripts/compile.sh
exec java -cp build/classes digitallibrary.Main "${1:-8080}"
