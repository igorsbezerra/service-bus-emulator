#!/bin/bash

# Wait for Azure SQL Edge to be ready
until nc -z sqledge 1433; do
  echo "Waiting for Azure SQL Edge..."
  sleep 5
done
echo "Azure SQL Edge is up!"

# Wait for Azure Service Bus to be ready
until nc -z service-bus 5672; do
  echo "Waiting for Azure Service Bus..."
  sleep 5
done
echo "Azure Service Bus is up!"