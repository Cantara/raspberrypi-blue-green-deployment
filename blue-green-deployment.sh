#!/bin/sh
# Start the first node.
./scripts/start-primary.sh 5050 &

#Start a new node which will assume primary role.
./scripts/start-candidate.sh 5051 http://localhost:5050/
