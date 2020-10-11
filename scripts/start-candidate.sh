#!/bin/sh
SERVICE_PORT=${1:-5051}
PRIMARY_URL=${2:-http://localhost:5050/}
java -DprimaryUrl=$PRIMARY_URL -Dservice_port=$SERVICE_PORT -jar target/blueGreenService.jar
