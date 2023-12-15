FROM nginx:latest

USER root

RUN apt update && apt install -y sudo && apt install -y ssh && apt install -y sshpass && apt install -y lsof && apt install -y wget && apt install net-tools
RUN apt install coreutils
RUN sudo mkdir -p /opt/java
RUN wget https://download.oracle.com/java/17/latest/jdk-17_linux-x64_bin.tar.gz
RUN tar -zxvf jdk-17_linux-x64_bin.tar.gz

RUN sudo mv jdk-17* /opt/java
#환경 변수 설정
RUN echo 'export JAVA_HOME=/opt/java/jdk-17.0.9' >> ~/.bashrc
RUN echo 'export PATH=$JAVA_HOME/bin:$PATH' >> ~/.bashrc

#CMD ["cd /home"]
#CMD ["touch test.txt"]
#CMD ["./mvnw clean package -DskipTests"]
#ENTRYPOINT ["sudo chmod 400 /home/entrypoint.sh"]
#CMD ./home/entrypoint.sh
#ENTRYPOINT ["sudo", "source", "~/.bashrc"]