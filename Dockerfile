#Construindo um container com a API e rodando ele

FROM maven:3.8.3-openjdk-17 as build
COPY . .
RUN mvn clean package -DskipTeste


FROM openjdk:17-jdk
COPY --from=build /target/Spring_NoSQL-0.0.1-SNAPSHOT.jar springNoSql.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "springNoSql.jar"]
