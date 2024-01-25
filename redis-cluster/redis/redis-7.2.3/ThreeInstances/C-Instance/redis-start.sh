#!/usr/bin/env bash



docker-compose -f redis-server.yml down --rmi all
docker-compose -f redis-server.yml build
docker-compose -f redis-server.yml up -d
docker rmi -f $(docker images -f "dangling=true" -q)

