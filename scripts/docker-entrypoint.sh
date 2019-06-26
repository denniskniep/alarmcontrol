#!/bin/sh
set -e

# Source: https://github.com/docker-library/official-images#consistency

# this will check if the first argument is a flag
# but only works if all arguments require a hyphenated flag
# -v; -SL; -f arg; etc will work, but not arg1 arg2
if [ "$#" -eq 0 ] || [ "${1#-}" != "$1" ]; then
    set -- java "$@"
fi

# check for the expected command
if [ "$1" = 'java' ]; then
    # init stuff...

    # use gosu (or su-exec) to drop to a non-root user
    exec gosu appuser "$@"
fi

# else default to run whatever the user wanted like "bash" or "sh"
exec "$@"