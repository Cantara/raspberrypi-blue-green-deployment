#!/bin/sh

# Config
mkdir -p "blue"
mkdir -p "green"
cp target/blueGreenService.jar blue
cp target/blueGreenService.jar green
# Start the first node.
sh ./scripts/start-primary.sh blue 5050 > /dev/null

#Start a new node which will assume primary role.
sh ./scripts/start-candidate.sh green 5051 http://localhost:5050/ FALLBACK
sh ./scripts/find-processIds.sh
