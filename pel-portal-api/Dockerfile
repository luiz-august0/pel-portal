# Stage 1: Build
FROM maven:3.9.11-eclipse-temurin-25-alpine AS builder
WORKDIR /workdir/server
COPY pom.xml /workdir/server/pom.xml
COPY .mvn /workdir/server/.mvn
COPY mvnw /workdir/server/mvnw
RUN ./mvnw dependency:go-offline

COPY src /workdir/server/src
RUN ./mvnw clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:25-jre-alpine
WORKDIR /work/application

EXPOSE 8081
COPY --from=builder /workdir/server/target/quarkus-app/lib/ /work/application/lib/
COPY --from=builder /workdir/server/target/quarkus-app/*.jar /work/application/
COPY --from=builder /workdir/server/target/quarkus-app/app/ /work/application/app/
COPY --from=builder /workdir/server/target/quarkus-app/quarkus/ /work/application/quarkus/

ENTRYPOINT ["java","-jar","/work/application/quarkus-run.jar"]