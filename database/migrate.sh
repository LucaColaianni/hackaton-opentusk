#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")/.."
for migration in database/migrations/V*.sql; do
  docker compose exec -T postgres psql -v ON_ERROR_STOP=1 -U root -d opentusk < "$migration"
done
