FROM eclipse-temurin:17-jre-alpine
COPY selfcloud-announcement-application/target/selfcloud-announcement-application-1.0.8.jar selfcloud-announcement-1.0.8.jar
EXPOSE 8091
ENTRYPOINT ["java","-jar","/selfcloud-announcement-1.0.8.jar"]