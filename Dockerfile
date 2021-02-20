FROM adoptopenjdk/openjdk11:latest
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE=target/m2.jar
ADD ${JAR_FILE} login.jar
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar login.jar" ]