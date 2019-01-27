 FROM gcr.io/google-appengine/openjdk:8

 COPY target/uber.jar uber.jar
 COPY resource/config.yml resource/config.yml
 CMD [ "java", "-jar","uber.jar", "server", "resource/config.yml"]