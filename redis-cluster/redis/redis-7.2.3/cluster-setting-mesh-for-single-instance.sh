#!/usr/bin/env bash

ipAddress=""

ipAddress=$(curl ifconfig.me)

yes "yes" | redis-cli --cluster create $ipAddress:6300 $ipAddress:6301 $ipAddress:6302

redis-cli --cluster add-node $ipAddress:6400 $ipAddress:6300 --cluster-slave
redis-cli --cluster add-node $ipAddress:6401 $ipAddress:6301 --cluster-slave
redis-cli --cluster add-node $ipAddress:6402 $ipAddress:6302 --cluster-slave