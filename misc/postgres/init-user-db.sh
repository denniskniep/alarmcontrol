#!/bin/bash
set -e

echo
echo 'Creating keycloak user and database...'
echo

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
    CREATE USER keycloak WITH PASSWORD '8uV5Z7GXg1u9Sw';
    CREATE DATABASE keycloak;
    GRANT ALL PRIVILEGES ON DATABASE keycloak TO keycloak;
EOSQL
