FROM amazoncorretto:21.0.3
COPY target/players-*.jar players.jar
ENTRYPOINT ["java", "-jar", "players.jar"]
