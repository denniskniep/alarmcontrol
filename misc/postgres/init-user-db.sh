#!/bin/bash
set -e

echo
echo 'Creating keycloak user and database...'
echo

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
    CREATE USER keycloak WITH PASSWORD 'xxx';
    CREATE DATABASE keycloak;
    GRANT ALL PRIVILEGES ON DATABASE keycloak TO keycloak;
EOSQL
