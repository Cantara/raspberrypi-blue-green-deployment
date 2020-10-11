#!/bin/bash

# Initial setup
#./start-nodes.sh

# Config
primaryNode=$(./scripts/isPrimaryGreenOrBlue.sh)
echo Primary: $primaryNode
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
nohup sh ./scripts/kill-node.sh $NODE_DIR
#Start a new node which will assume primary role.
#echo "Start: ./scripts/start-candidate.sh $NODE_DIR $NODE_PORT http://localhost:$PRIMARY_PORT/blueGreenService/bluegreenstatus/requestPrimary"
sh ./scripts/start-candidate.sh $NODE_DIR $NODE_PORT http://localhost:$PRIMARY_PORT/blueGreenService/bluegreenstatus/requestPrimary
sleep 2
echo
echo Find current ProcessIds
sh ./scripts/find-processIds.sh
echo
echo
newNodeHealth=$(curl -s http://localhost:$NODE_PORT/blueGreenService/health)
echo New Primary Health: $newNodeHealth
sleep 1
newFallbackHealth=$(curl -s http://localhost:$PRIMARY_PORT/blueGreenService/health)
echo New Fallback Health: $newFallbackHealth
echo
echo
primaryNode=$(./scripts/isPrimaryGreenOrBlue.sh)
echo Updated Primary to: $primaryNode

