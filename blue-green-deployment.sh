#!/bin/bash

# Initial setup
#./start-nodes.sh

# Config
fallbackNode=$(./scripts/isFallbackGreenOrBlue.sh)
echo Fallback: $fallbackNode
NODE_DIR=none
NODE_PORT=none
PRIMARY_PORT=none

if [[ $fallbackNode == 'blue' ]]; then
  NODE_DIR=blue
  NODE_PORT=5050
  PRIMARY_PORT=5051
elif [[ $fallbackNode == 'green' ]]; then
  NODE_DIR=green
  NODE_PORT=5051
  PRIMARY_PORT=5050
else
  echo No fallback node found exiting.
  exit 1
fi
cp target/blueGreenService.jar $NODE_DIR
# Kill the node
sh ./scripts/kill-node.sh $NODE_DIR
#Start a new node which will assume primary role.
sh ./scripts/start-candidate.sh $NODE_DIR $NODE_PORT 5051 http://localhost:$PRIMARY_PORT/
sh ./scripts/find-processIds.sh
