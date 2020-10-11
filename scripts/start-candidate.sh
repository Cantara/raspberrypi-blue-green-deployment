#!/bin/sh
NODE_DIR=${1:-green}
SERVICE_PORT=${2:-5051}
PRIMARY_URL=${3:-http://localhost:5050/}
nohup java -DprimaryUrl=$PRIMARY_URL -Dservice.port=$SERVICE_PORT -jar $NODE_DIR/blueGreenService.jar &
