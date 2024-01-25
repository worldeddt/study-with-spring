#!/usr/bin/env bash


redis-cli --cluster create A-SERVER-IP:6300 B-SERVER-IP:6301 C-SERVER-IP:6302

redis-cli --cluster add-node A-SERVER-IP:6400 B-SERVER-IP:6301 --cluster-slave
redis-cli --cluster add-node C-SERVER-IP:6402 A-SERVER-IP:6300 --cluster-slave
redis-cli --cluster add-node B-SERVER-IP:6401 C-SERVER-IP:6302 --cluster-slave
