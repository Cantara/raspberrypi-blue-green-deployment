#!/bin/sh
SERVICE_PORT=${1:-5050}
java -Dservice.port=$SERVICE_PORT -jar target/blueGreenService.jar
