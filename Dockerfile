FROM amazoncorretto:21.0.2
COPY target/players-*.jar players.jar
ENTRYPOINT ["java", "-jar", "players.jar"]
