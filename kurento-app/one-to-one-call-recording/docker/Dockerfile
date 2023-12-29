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

ENV JAVA_HOME=/opt/java/jdk-17.0.9
ENV PATH=$JAVA_HOME/bin:$PATH

ENTRYPOINT ["./kurento/mvnw", "clean" ,"package", "-DskipTests"]
ENTRYPOINT ["nohup", "java", "-jar", "./kurento/target/one-to-one-call-recording-0.0.1.jar", "&"]