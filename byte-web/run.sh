#!/bin/bash

# Set a default IP address
DEFAULT_IP="121.250.209.74"

# Use the first argument as the IP address, or use the default if no argument is provided
IP_ADDRESS=${1:-$DEFAULT_IP}

echo "Ip address is: $IP_ADDRESS"

set -e

sudo docker rm -f byte-todo-server

echo "byte-todo-server deleted"

sudo docker rmi -f byte-todo-server

echo "byte-todo-server image deleted"

sudo docker build -f ./Dockerfile -t byte-todo-server .

echo "build byte-todo-server successfully!!!"

sudo docker run -d -p 8090:8090 \
 -e DB_URL="jdbc:mysql://$IP_ADDRESS:3306/qingxun?autoReconnect=true&useSSL=false&serverTimezone=Asia/Shanghai&useUnicode=true&lowerCaseTableNames=1&characterEncoding=utf8" \
 -e DB_USERNAME="root" -e DB_PASSWORD="cong0917" \
 -e REDIS_HOST="$IP_ADDRESS" -e REDIS_PORT="6379" \
 -v /mnt/logs/plcs-server:/mnt/logs/plcs-server \
 --name byte-todo-server byte-todo-server:latest

echo "byte-todo-server run successfully"
