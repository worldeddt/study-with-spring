#!/usr/bin/env sh

echo 'vm.overcommit_memory = 1' >> /etc/sysctl.conf
#echo 1 >/proc/sys/vm/overcommit_memory
sudo sysctl vm.overcommit_memory=1
redis-server /redis/redis.conf
