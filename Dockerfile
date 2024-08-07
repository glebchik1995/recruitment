FROM openjdk:21-slim AS build
WORKDIR /
COPY src src/
COPY checkstyle.xml /
COPY pom.xml /
RUN apt-get update && apt-get install -y maven
RUN mvn clean install -DskipTests && apt-get clean

FROM openjdk:21-slim
COPY --from=build /target/*.jar application.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "application.jar"]


#FROM openjdk:21-slim AS build
#WORKDIR /opt/app
#COPY mvnw pom.xml ./
#COPY src src/
#RUN apt-get update && apt-get install -y maven
#RUN mvn clean install -DskipTests && apt-get clean
#
#FROM openjdk:21-slim
#WORKDIR /opt/app
#EXPOSE 8080
#COPY --from=build /opt/app/target/*.jar /opt/app/app.jar
#LABEL author="Gleb"
#LABEL description="Description of your application"
#LABEL version="1.0"
#ENTRYPOINT ["java", "-jar", "/opt/app/app.jar"]