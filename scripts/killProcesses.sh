#!/bin/sh
#ps -ef | grep blueGreenService.jar
kill $(ps aux | grep 'blueGreenService.jar'  | awk '{print $2}')