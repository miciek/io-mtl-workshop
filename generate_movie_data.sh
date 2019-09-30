#!/bin/bash

content=$(cat src/main/resources/movie_metadata_1.csv)
big=src/main/resources/movie_metadata_2.csv
fail=src/main/resources/movie_metadata_3.csv

echo "$content" > "$big"
for i in {1..200}; do
  echo "$content" >> "$big"
done

echo "$content" > "$fail"
echo "Color,NOPE" >> "$fail"
