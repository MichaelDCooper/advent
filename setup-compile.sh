#!/bin/bash

# Setup script for compile_commands.json generation

# Install Bear if not present 
# brew install bear

# Create a master Makefile that includes all days
generate_master_makefile() {
    echo "# Auto-generated Makefile for Advent of Code" > Makefile
    echo "all:" >> Makefile
    
    # Find all .cpp files and add them to the Makefile
    find . -name "*.cpp" | while read -r file; do
        dir=$(dirname "$file")
        base=$(basename "$file" .cpp)
        echo -e "\t@mkdir -p ${dir}/bin" >> Makefile
        echo -e "\t@g++ -std=c++17 -Wall -Wextra ${file} -o ${dir}/bin/${base}" >> Makefile
    done
}

# Generate compile_commands.json using Bear
generate_compile_commands() {
    # Generate the Makefile first
    generate_master_makefile
    
    # Use Bear to generate compile_commands.json
    bear -- make
    
    # Clean up temporary Makefile
    rm Makefile
}

# Main execution
echo "Generating compile_commands.json..."
generate_compile_commands
echo "Done! compile_commands.json has been created at the project root."
