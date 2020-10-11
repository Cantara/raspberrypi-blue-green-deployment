#!/bin/sh

# Cleanup
sh ./scripts/kill-all-nodes.sh
sleep 3

# Config
mkdir -p "blue"
mkdir -p "green"
cp target/blueGreenService.jar blue
cp target/blueGreenService.jar green
# Start the first node.
sh ./scripts/start-primary.sh blue 5050 > /dev/null
sleep 3
curl  http://localhost:5050/blueGreenService/health

# To start a node in FALLBACK
#sh ./scripts/start-candidate.sh green 5051 http://localhost:5050/ FALLBACK

# Start a new node which will assume primary role.
sh ./scripts/start-candidate.sh green 5051 http://localhost:5050/blueGreenService/bluegreenstatus/requestPrimary
sleep 3
curl  http://localhost:5051/blueGreenService/health
sh ./scripts/find-processIds.sh

# Verify one is Primary
bash ./scripts/isPrimaryGreenOrBlue.sh
