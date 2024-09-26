FROM maven:3-openjdk-17 as builder

WORKDIR /tmp/app

COPY target/blog-watcher-0.0.1.jar blog-watcher-0.0.1.jar

COPY .env .env

EXPOSE 8081


ENTRYPOINT ["java", "-jar", "blog-watcher-0.0.1.jar"]
