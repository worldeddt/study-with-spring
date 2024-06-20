#!/bin/sh

echo "====== entry point start ======"

LOG_FILE="/app/as/application.log"

touch @LOG_FILE

nohup java -jar /app/as/as.jar --spring.config.location=/app/as/as.yml &
nohup java -jar /app/cti/cti.jar --spring.config.location=/app/cti/cti.yml &
nohup java -jar /app/cipher/cipher.jar --spring.config.location=/app/cipher/cipher.yml &

echo "Application started a" >> /app/as/application.log

tail -f /dev/null