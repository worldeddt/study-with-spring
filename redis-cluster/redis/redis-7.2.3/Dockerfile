FROM redis:7.2.3

WORKDIR /data

RUN apt-get update && \
    apt-get install -y procps && \
    apt-get install -y apt && \
    apt install -y net-tools && \
    apt install sudo && \
    ls -al

COPY entrypoint.sh /redis/entrypoint.sh
RUN chmod +x /redis/entrypoint.sh

ENTRYPOINT ["sh", "-c", "cd /redis && sudo redis-server redis.conf --daemonize yes & tail -f /dev/null"]