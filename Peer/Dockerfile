#FROM maven:3.2-jdk-7-onbuild
#CMD mvn package

#FROM java:8
#ADD /target/block-chain-1.0-SNAPSHOT.jar app.jar
#ENTRYPOINT ["java", "-cp", "app.jar", "com.databasserne.App"]

FROM openjdk:8
ADD /target/block-chain-1.0-SNAPSHOT.jar app.jar
CMD ["/usr/bin/java", "-cp", "app.jar", "com.databasserne.App"]
