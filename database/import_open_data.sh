#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")/.."
docker compose cp database/data/monitoraggio-tempi-di-attesa-2024-10-07_11.csv postgres:/tmp/mosalute_monitoraggio.csv
docker compose exec -T postgres psql -v ON_ERROR_STOP=1 -U root -d opentusk < database/import_open_data.sql
