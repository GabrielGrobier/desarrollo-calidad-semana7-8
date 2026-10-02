#!/usr/bin/env bash
set -euo pipefail
# El profile test usa H2. No necesita docker compose ni red externa.
mvn clean verify
printf '\nReportes: target/surefire-reports/ y target/site/jacoco/index.html\n'
