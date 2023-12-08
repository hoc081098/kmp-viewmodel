#!/bin/bash

# Read input from arguments.
input=$1

if [[ "$input" == "snapshot" ]]; then
    brew install gnu-sed
    gsed -i 's/]: https:\/\/hoc081098.github.io\/kmp-viewmodel\/docs\/0.x/]: https:\/\/hoc081098.github.io\/kmp-viewmodel\/docs\/latest/g' docs/index.md
    echo "Updated index.md file with snapshot version."
else
    echo "Invalid input: $input"
    exit 1
fi

