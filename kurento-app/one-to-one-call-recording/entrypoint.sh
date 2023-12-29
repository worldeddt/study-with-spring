#!/usr/bin/env sh

#./kurento/mvnw clean package -DskipTests -X
echo $JAVA_HOME
java
nohup java -jar ./kurento/target/one-to-one-call-recording-0.0.1.jar &
