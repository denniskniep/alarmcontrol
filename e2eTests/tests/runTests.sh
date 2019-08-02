#!/bin/bash

# Exit immediately if a command exits with a non-zero status.
set -e

# Treat unset variables as an error when substituting.
set -u

SCRIPT_PATH=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )
cd "${SCRIPT_PATH}"
URL="http://alarmcontrol:8080/graphql"  ./initialSetup.sh
ls | grep '.js' | xargs -I@ -t node @
cd -