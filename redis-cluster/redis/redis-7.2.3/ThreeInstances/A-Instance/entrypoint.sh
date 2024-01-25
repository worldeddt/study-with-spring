#!/usr/bin/env bash

#chmod 777 /etc/sysctl.conf
#echo 'vm.overcommit_memory = 1' >> /etc/sysctl.conf
#sudo sysctl -p
#echo 1 >/proc/sys/vm/overcommit_memory
#sudo sysctl vm.overcommit_memory=1

redis-server redis.conf --daemonize yes
tail -f /dev/null