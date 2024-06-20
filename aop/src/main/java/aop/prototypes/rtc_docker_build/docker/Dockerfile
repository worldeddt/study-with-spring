FROM openjdk:11
WORKDIR /app
COPY ../coview-as-hwgi/build/libs/*.jar /app/as/as.jar
COPY ../coview-cipher-hwgi/build/libs/*.jar /app/cipher/cipher.jar
COPY ../coview-cti-hwgi/build/libs/*.jar /app/cti/cti.jar
COPY ../coview-as-hwgi/src/main/resources/v21as-dev.yml /app/as/as.yml
COPY ../coview-cipher-hwgi/src/main/resources/cipher-dev.yml /app/cipher/cipher.yml
COPY ../coview-cti-hwgi/src/main/resources/cti-dev.yml /app/cti/cti.yml
COPY ../docker/entrypoint.sh /app/entrypoint.sh

RUN chmod +x /app/entrypoint.sh

CMD ["apt update"]
CMD ["apt-get update"]
CMD ["apt-get install -y net-tools"]
CMD ["apt-get install -y iputils-ping"]
CMD ["apt install -y procps"]

ENTRYPOINT ["/bin/sh" ,"/app/entrypoint.sh" ]

