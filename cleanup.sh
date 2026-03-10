#!/bin/bash
# Clean up IDE cache and force Maven refresh

echo "Cleaning up IntelliJ IDE caches..."
rm -rf /Users/ericross/Documents/futureBagg/git/PatternsInterview/Car/.idea/libraries/
rm -rf /Users/ericross/Documents/futureBagg/git/PatternsInterview/Car/out/
rm -rf /Users/ericross/Documents/futureBagg/git/PatternsInterview/Car/target/

echo "Cleaning Maven cache for this project..."
rm -rf ~/.m2/repository/org/springframework/
rm -rf ~/.m2/repository/org/springframework/boot/

echo "Done! Now:"
echo "1. Restart IntelliJ IDEA"
echo "2. Go to View > Tool Windows > Maven"
echo "3. Click the Reload Projects button (circular arrow icon)"
echo "4. Wait for Maven to download all dependencies"

