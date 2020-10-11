#!/bin/bash
BLUE_PORT=${1:-5050}
GREEN_PORT=${2:-5051}
blueResponse=$(curl -s "http://localhost:$BLUE_PORT/blueGreenService/health")
greenResponse=$(curl -s "http://localhost:$GREEN_PORT/blueGreenService/health")
if [[ $blueResponse == *'PRIMARY'* ]]; then
  echo blue
elif [[ $greenResponse == *'PRIMARY'* ]]; then
  echo green
else
  echo none
fi
