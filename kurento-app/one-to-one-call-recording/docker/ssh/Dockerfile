FROM nginx:latest

RUN apt-get update && \
    apt-get install -y openssh-server && \
    mkdir /var/run/sshd

# SSH 계정 및 비밀번호 설정
#RUN useradd -m -s /bin/bash fermi && \
#    echo 'fermi:fermi1004@!@!' | chpasswd

#RUN ssh -L 8888:localhost:22 fermi@

# SSH 포트 열기
EXPOSE 8888

CMD ["/usr/sbin/sshd", "-D"]