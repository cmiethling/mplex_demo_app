#!/bin/bash

# Start client in the background
(cd client && mvn spring-boot:run) &
CLIENT_PID=$!

# Start emulator in the background
(cd emulator && mvn spring-boot:run) &
EMULATOR_PID=$!

# Function to stop both processes
function stop_processes {
  echo "Stopping client and emulator..."
  kill $CLIENT_PID $EMULATOR_PID
  wait $CLIENT_PID $EMULATOR_PID
  echo "Stopped."
}

# Trap SIGINT (Ctrl+C) and call stop_processes
trap stop_processes SIGINT

# Wait for both processes to finish
wait $CLIENT_PID $EMULATOR_PID