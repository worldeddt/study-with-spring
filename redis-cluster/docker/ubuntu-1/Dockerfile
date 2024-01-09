FROM nginx:latest

RUN apt update && \
    apt install -y sudo &&  \
    apt install -y ssh &&  \
    apt install -y sshpass &&  \
    apt install -y lsof &&  \
    apt install -y wget &&  \
    apt install net-tools &&  \
    apt install coreutils &&  \
    sudo mkdir -p /opt/java && \
    sudo apt install -y nmap
RUN wget https://download.oracle.com/java/17/latest/jdk-17_linux-x64_bin.tar.gz
RUN tar -zxvf jdk-17_linux-x64_bin.tar.gz
RUN sudo apt install -y zsh
RUN sudo mv jdk-17* /opt/java
RUN sudo apt install -y nmap
RUN sudo apt install make
RUN sudo apt-get update
RUN sudo apt install -y pkg-config
RUN sudo apt-get install -y build-essential
RUN sudo apt-get install -y libjemalloc-dev
RUN sudo apt-get install -y tcl
RUN sudo apt-get install -y liblua5.3-dev

#CMD ["cd", "/app"]
#CMD ["cd", "/redis"]
#CMD ["cd", "/redis-7.2.3"]
#CMD ["make"]
#ENTRYPOINT["mkdir", "logs"]
#ENTRYPOINT["cd", "/src"]
#ENTRYPOINT["sudo", "make", "install"]


#CMD ["cd", "/src"]
#CMD ["sudo", "make", "install"]
#CMD ["redis-server", "redis_6300.conf"]
#CMD ["redis-server", "redis_6301.conf"]
#CMD ["redis-server", "redis_6302.conf"]
#CMD ["redis-server", "redis_6400.conf"]
#CMD ["redis-server", "redis_6401.conf"]
#CMD ["redis-server", "redis_6402.conf"]

#RUN redis-server redis_6300.conf && \
#    redis-server redis_6301.conf && \
#    redis-server redis_6302.conf && \
#    redis-server redis_6400.conf && \
#    redis-server redis_6401.conf && \
#    redis-server redis_6402.conf

# master node 등록
# redis-cli --cluster create 127.0.0.1:6300 127.0.0.1:6301 127.0.0.1:6302

# slave node 연결
# redis-cli --cluster add-node 127.0.0.1:6400 127.0.0.1:6300 --cluster-slave
# redis-cli --cluster add-node 127.0.0.1:6401 127.0.0.1:6301 --cluster-slave
# redis-cli --cluster add-node 127.0.0.1:6402 127.0.0.1:6302 --cluster-slave

ENV JAVA_HOME=/opt/java/jdk-17.0.9
ENV PATH=$JAVA_HOME/bin:$PATH