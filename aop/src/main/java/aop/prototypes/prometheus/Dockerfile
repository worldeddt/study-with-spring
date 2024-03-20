FROM openjdk:21-jdk

WORKDIR /app

#COPY ./coview_lgw_server-1.0.0.jar /app/coview_lgw_server-1.0.0.jar

CMD ["nohup", "java", "-jar", "-Dspring.profiles.active=local", "coview_lgw_server-1.0.0.jar", "&"]