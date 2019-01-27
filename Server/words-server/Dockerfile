 FROM gcr.io/google-appengine/openjdk:8

 COPY target/words-server-1.0-SNAPSHOT-jar-with-dependencies.jar words-server-1.0-SNAPSHOT-jar-with-dependencies.jar
 COPY resource/config.yml resource/config.yml


 CMD [ "java", "-jar","words-server-1.0-SNAPSHOT-jar-with-dependencies.jar", "server", "resource/config.yml"]