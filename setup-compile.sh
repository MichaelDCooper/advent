#!/bin/bash

generate_master_makefile() {
    echo "# Auto-generated Makefile for Advent of Code" > Makefile
    echo ".PHONY: all clean" >> Makefile
    echo "all: build" >> Makefile
    echo "build:" >> Makefile
    
    # Find all .cpp files
    find . -type f -name "*.cpp" | while read -r file; do
        dir=$(dirname "$file")
        base=$(basename "$file" .cpp)
        target="${dir}/bin/${base}"
        
        echo "Adding: $file -> $target"
        
        # Add compilation rule using clang++
        echo -e "\t@mkdir -p ${dir}/bin" >> Makefile
        echo -e "\t@clang++ -std=c++17 -Wall -Wextra" \
             "-I${dir} " \
             "-c ${file} " \
             "-o ${target}" >> Makefile
    done
    
    # Add clean target
    echo "clean:" >> Makefile
    echo -e "\t@find . -type d -name bin -exec rm -rf {} +" >> Makefile
}

echo "Generating Makefile..."
generate_master_makefile

echo "Generating compile_commands.json..."
bear -- make

echo "Cleaning up..."
rm Makefile

echo "Done! compile_commands.json has been created."
