# Stage 1: Build
FROM maven:3.9.5-eclipse-temurin-21-alpine as builder
WORKDIR /workdir/server
COPY pom.xml /workdir/server/pom.xml
RUN mvn dependency:go-offline

COPY src /workdir/server/src
RUN mvn clean install -DskipTests

# Stage 2: Prepare for production
FROM builder as prepare-production
RUN mkdir -p target/dependency
WORKDIR /workdir/server/target/dependency
RUN jar -xf ../*.jar

# Stage 3: Runtime
FROM openjdk:22-ea-21-jdk

EXPOSE 8082
VOLUME /tmp
ARG DEPENDENCY=/workdir/server/target/dependency
COPY --from=prepare-production ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=prepare-production ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=prepare-production ${DEPENDENCY}/BOOT-INF/classes /app

ENTRYPOINT ["java","-cp","app:app/lib/*","com.almeja.pel.gestao.GestaoApplication"]