FROM maven:3-openjdk-17 as builder

WORKDIR /tmp/app

ADD . .

RUN mvn package -Dmaven.test.skip

EXPOSE 8081

WORKDIR /tmp/app/target

ENTRYPOINT ["java", "-jar", "blog-watcher-0.0.1.jar"]
