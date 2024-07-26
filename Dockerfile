FROM eclipse-temurin:17-jre-alpine
MAINTAINER selfcloud.pl
COPY target/selfcloud-announcement-1.0.5.jar selfcloud-announcement-1.0.5.jar
EXPOSE 8091
ENTRYPOINT ["java","-jar","/selfcloud-announcement-1.0.5.jar"]