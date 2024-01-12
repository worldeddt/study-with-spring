FROM redis:7.2.3

RUN apt-get update && \
    apt-get install -y procps && \
    apt-get install -y apt && \
    apt install -y net-tools && \
    apt install sudo

#ENTRYPOINT ["cd /data", "&&", "./entrypoint.sh"]