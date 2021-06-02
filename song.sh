#!/bin/bash
echo "begin"
./gradlew assembleDebug
for file in ./app/build/outputs/apk/debug/*
do
  if echo "$file" | grep -q -E '\.apk$'
  then
    adb install -r $file
  fi
done

