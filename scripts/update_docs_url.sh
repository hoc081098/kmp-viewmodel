#!/bin/bash

# Read input from arguments.
input=$1

if [[ "$input" == "snapshot" ]]; then
    brew install gnu-sed
    gsed -i "s/0.x/latest/g" docs/index.md
    echo "Updated index.md file with snapshot version."
else
    echo "Invalid input: $input"
    exit 1
fi

