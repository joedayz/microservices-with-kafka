FROM openjdk:11.0.2-jre-slim
COPY target/microservice-kafka-invoicing-1.0-SNAPSHOT.jar .
CMD /usr/bin/java -Xmx400m -Xms400m  -XX:TieredStopAtLevel=1 -noverify -jar microservice-kafka-invoicing-1.0-SNAPSHOT.jar
EXPOSE 8080
