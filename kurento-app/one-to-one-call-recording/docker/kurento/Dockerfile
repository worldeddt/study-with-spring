FROM kurento/kurento-media-server:latest

RUN apt update && \
    apt install -y sudo &&  \
    apt install -y ssh &&  \
    apt install -y sshpass &&  \
    apt install -y lsof &&  \
    apt install net-tools

EXPOSE 8888
