FROM gradle:jdk17 AS bootJar
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

FROM openjdk:17.0-jdk-slim
RUN mkdir /app
#ENV DB_URL = jdbc:postgresql://localhost:5432/wallet
#ENV DB_USER = root
#ENV DB_PASS = root
COPY --from=bootJar /home/gradle/src/build/libs/*.jar /app/
ENV JAVA_OPTS=""
CMD java ${JAVA_OPTS} -jar /app/wallet-be.jar
