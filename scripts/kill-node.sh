#!/bin/sh
#ps -ef | grep blueGreenService.jar
NODE_DIR=${1:-nonexisting}
echo Kill prossess in dir: $NODE_DIR
node=$(ps aux |grep $NODE_DIR'/blueGreenService.jar' | grep 'jar')
echo Node $node
process=$(ps aux | grep $NODE_DIR'/blueGreenService.jar'  | awk '{print $2}')
echo Process $process
kill $process