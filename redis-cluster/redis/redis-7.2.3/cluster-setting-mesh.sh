#!/usr/bin/env bash


redis-cli --cluster create 20.214.111.167:6300 20.214.111.167:6301 20.214.111.167:6302 20.214.181.249:6300 20.214.181.249:6301 20.214.181.249:6302

redis-cli --cluster add-node 20.214.181.249:6400 20.214.111.167:6300 --cluster-slave
redis-cli --cluster add-node 20.214.181.249:6401 20.214.111.167:6301 --cluster-slave
redis-cli --cluster add-node 20.214.181.249:6402 20.214.111.167:6302 --cluster-slave

redis-cli --cluster add-node 20.214.111.167:6400 20.214.181.249:6300 --cluster-slave
redis-cli --cluster add-node 20.214.111.167:6401 20.214.181.249:6301 --cluster-slave
redis-cli --cluster add-node 20.214.111.167:6402 20.214.181.249:6302 --cluster-slave
