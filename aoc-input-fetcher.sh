#!/bin/bash

# This implementation is coupled to my project dir. 
# Saves file to /${YEAR}/${DAY}/input.txt
#
# To fetch AOC_SESSION: 
# 1. Go to AOC website
# 2. Navigate to your browsers dev tools 
# 3. Go to Application/Storage/Cookies
# 4. Find value for "session" and copy 
# 5. In terminal run `export AOC_SESSION='your_session_cookie'`

# Check if the required environment variable is set
if [ -z "$AOC_SESSION" ]; then
    echo "Please set your AOC_SESSION environment variable with your session cookie"
    echo "Example: export AOC_SESSION='your_session_cookie'"
    exit 1
fi

# Check if day and year parameters are provided
if [ "$#" -ne 2 ]; then
    echo "Usage: $0 <day> <year>"
    echo "Example: $0 1 2023"
    exit 1
fi

DAY=$1
YEAR=$2

# Validate input
if ! [[ "$DAY" =~ ^[1-9][0-9]?$|^25$ ]]; then
    echo "Day must be between 1 and 25"
    exit 1
fi

if ! [[ "$YEAR" =~ ^20[1-9][0-9]$ ]]; then
    echo "Year must be 2015 or later"
    exit 1
fi

# Create URL
URL="https://adventofcode.com/$YEAR/day/$DAY/input"

# Create directory structure
DIR="$YEAR"
mkdir -p "$DIR"

# Fetch input and save to file
echo "Fetching input for Day $DAY of $YEAR..."
curl -s --cookie "session=$AOC_SESSION" "$URL" > "$DIR/input.txt"

# Check if the fetch was successful
if [ $? -eq 0 ] && [ -s "$DIR/input.txt" ]; then
    echo "Input successfully saved to $DIR/input.txt"
else
    echo "Error fetching input. Please check your session cookie and internet connection"
    rm -f "$DIR/input.txt"
    exit 1
fi
