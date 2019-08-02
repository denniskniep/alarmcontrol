#!/bin/bash

# Exit immediately if a command exits with a non-zero status.
set -e

# Treat unset variables as an error when substituting.
set -u

start=$(date +%s)

# Create TestOutput Folder
SCRIPT_PATH=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )
TEST_OUTPUT_PATH=/tmp/alarmcontrol_testoutput/$(uuidgen)
mkdir -p "${TEST_OUTPUT_PATH}"
printf "TestOutputFolder: %s\n" "${TEST_OUTPUT_PATH}"

# Change permission that user inside container can store the output
sudo chmod o+rw "${TEST_OUTPUT_PATH}"

printf "Starting Application...\n"
sudo \
COMPOSE_PROJECT_NAME="e2e-Tests" \
DATABASE_NAME="test" \
DATABASE_USER="test" \
DATABASE_PASSWORD="test" \
GRAPHHOPPER_APIKEY="${GRAPHHOPPER_APIKEY:-xxx}" \
MAPBOX_ACCESS_TOKEN="${MAPBOX_ACCESS_TOKEN:-xxx}" \
docker-compose -f "${SCRIPT_PATH}/../docker-compose-dev.yaml" up --build -d

printf "Starting End2End-Tests...\n"

# Do not exit immediately if the following command exits with a non-zero status, due to cleanup.
set +e

sudo \
COMPOSE_PROJECT_NAME="e2e-Tests" \
TEST_PATH="${SCRIPT_PATH}/tests" \
TEST_OUTPUT_PATH="${TEST_OUTPUT_PATH}" \
docker-compose -f "${SCRIPT_PATH}/docker-compose-testrunner.yaml" up --build
printf "End2End-Tests finished\n"

sudo \
COMPOSE_PROJECT_NAME="e2e-Tests" \
docker-compose -f "${SCRIPT_PATH}/../docker-compose-dev.yaml" logs --tail="all" --no-color --timestamps alarmcontrol  > "${TEST_OUTPUT_PATH}/logs.txt"

printf "Stopping Application...\n"
sudo \
COMPOSE_PROJECT_NAME="e2e-Tests" \
docker-compose -f "${SCRIPT_PATH}/../docker-compose-dev.yaml" down -v

end=$(date +%s)
runtime=$((end-start))

printf "#####################################\n"
printf "TestOutputFolder: %s\n" "${TEST_OUTPUT_PATH}"
printf "Runtime: %s sec.\n" "${runtime}"
printf "#####################################\n"
