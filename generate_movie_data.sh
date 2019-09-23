#!/bin/bash

content=$(cat src/main/resources/movie_metadata.csv)
big=src/main/resources/movie_metadata_big.csv
fail=src/main/resources/movie_metadata_fail.csv

echo "$content" > "$big"
for i in {1..200}; do
  echo "$content" >> "$big"
done

echo "$content" > "$fail"
echo "Color,NOPE" >> "$fail"
