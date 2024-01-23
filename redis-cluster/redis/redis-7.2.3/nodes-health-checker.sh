#!/usr/bin/env bash

# ./nodes-health-checker.sh 상대인스턴스공인IP > /dev/null 2>&1 & 로 서버내 명령어 실행 필요
pong6300=""
pong6301=""
pong6302=""
pong6400=""
pong6401=""
pong6402=""

PONG="PONG"

while true; do
  pong6300=$(redis-cli -h $1 -p 6300 ping) > /dev/null
  pong6301=$(redis-cli -h $1 -p 6301 ping) > /dev/null
  pong6302=$(redis-cli -h $1 -p 6302 ping) > /dev/null
  pong6400=$(redis-cli -h $1 -p 6400 ping) > /dev/null
  pong6401=$(redis-cli -h $1 -p 6401 ping) > /dev/null
  pong6402=$(redis-cli -h $1 -p 6402 ping) > /dev/null

  if [[ "$pong6300" == "$PONG" || "$pong6301" == "$PONG" || "$pong6302" == "$PONG" || "$pong6400" == "$PONG" ||
  "$pong6401" == "$PONG" || "$pong6402" == "$PONG" ]]; then
    echo "연결중"
  else
    echo $(date && echo $1 인스턴스 내 노드 다운) >> redis_down.log
    sh ./redis-start.sh
    sh ./cluster-setting-mesh-for-single-instance.sh
    break;
  fi

  sleep 5
done