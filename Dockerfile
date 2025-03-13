FROM openjdk:8

WORKDIR /app

VOLUME /app/tmp

COPY ./mvnw .
COPY ./.mvn .mvn
COPY ./pom.xml .

RUN ./mvnw dependency:go-offline

COPY ./src ./src

RUN ./mvnw clean package
CMD ["java","-XX:+PrintGCDetails" ,"-XX:+PrintGCDateStamps" ,"-Xloggc:gc.log", "-jar",   "./target/consultations-service-0.0.1-SNAPSHOT.jar"]