#!/usr/bin/env bash


redis-cli --cluster create DEVPT_IP:6300 DEVPT_IP:6301 DEVPT_IP:6302 DEMOPT_IP:6300 DEMOPT_IP:6301 DEMOPT_IP:6302

redis-cli --cluster add-node DEMOPT_IP:6400 DEVPT_IP:6300 --cluster-slave
redis-cli --cluster add-node DEMOPT_IP:6401 DEVPT_IP:6301 --cluster-slave
redis-cli --cluster add-node DEMOPT_IP:6402 DEVPT_IP:6302 --cluster-slave

redis-cli --cluster add-node DEVPT_IP:6400 DEMOPT_IP:6300 --cluster-slave
redis-cli --cluster add-node DEVPT_IP:6401 DEMOPT_IP:6301 --cluster-slave
redis-cli --cluster add-node DEVPT_IP:6402 DEMOPT_IP:6302 --cluster-slave
