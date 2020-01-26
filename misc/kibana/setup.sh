#!/bin/sh

URL=$(printf "%s\n" "$1")

if [ "$URL" = "" ]; then
  printf "Error: you need to provide a url\n" 1>&2;
  exit 2
fi

curl -v -X POST \
      -H "kbn-xsrf: true" \
      "$URL/api/saved_objects/_import" \
      --form file=@/app/saved-objects/savedObjects.ndjson