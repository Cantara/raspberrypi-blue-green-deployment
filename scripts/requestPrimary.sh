#!/bin/bash
BLUE_PORT=${1:-5050}
curl -i -X PUT -H "Content-Type:application/json" "http://localhost:$BLUE_PORT/blueGreenService/bluegreenstatus/requestPrimary"
