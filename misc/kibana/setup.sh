#!/bin/sh

echoerr() {
  if [ "$QUIET" -ne 1 ]; then printf "%s\n" "$*" 1>&2; fi
}

while [ $# -gt 0 ]
do
  case "$1" in
    *:* )
    URL=$(printf "%s\n" "$1")
    shift 1
    ;;
    -q | --quiet)
    QUIET=1
    shift 1
    ;;
    --)
    shift
    break
    ;;
    *)
    echoerr "Unknown argument: $1"
    usage 1
    ;;
  esac
done

if [ "$URL" = "" ]; then
  echoerr "Error: you need to provide a url"
  exit 2
fi

curl -v -X POST \
      -H "kbn-xsrf: true" \
      "$URL/api/saved_objects/_import" \
      --form file=@/app/saved-objects/savedObjects.ndjson