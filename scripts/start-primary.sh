#!/bin/sh
NODE_DIR=${1:-green}
SERVICE_PORT=${2:-5050}
nohup java -Dservice.port=$SERVICE_PORT -jar $NODE_DIR/blueGreenService.jar &
