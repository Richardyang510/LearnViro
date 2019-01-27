 FROM gcr.io/google-appengine/openjdk:8

 COPY target/words-server-1.0-SNAPSHOT.jar words-server-1.0-SNAPSHOT.jar
 COPY resource/config.yml resource/config.yml
 CMD [ "java", "-jar","words-server-1.0-SNAPSHOT.jar", "server", "resource/config.yml"]