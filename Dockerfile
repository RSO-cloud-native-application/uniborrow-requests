FROM adoptopenjdk:15-jre-hotspot

RUN mkdir /app

WORKDIR /app

ADD ./api/target/requests-api-1.0.0-SNAPSHOT.jar /app

EXPOSE 8081

CMD ["java", "-jar", "requests-api-1.0.0-SNAPSHOT.jar"]