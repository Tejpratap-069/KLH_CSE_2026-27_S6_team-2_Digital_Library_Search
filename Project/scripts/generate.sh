#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT"
COUNT="${1:-50000}"
SEED="${2:-2520030477}"
./scripts/compile.sh
java -cp build/classes digitallibrary.storage.DatasetGenerator "$COUNT" data/resources.txt "$SEED"
