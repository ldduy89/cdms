FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /app

COPY pom.xml ./
RUN mvn -q dependency:go-offline

COPY src src
RUN mvn -q -DskipTests package \
 && cp "$(ls target/*.jar | grep -v original | head -n 1)" /app/app.jar

FROM eclipse-temurin:25-jre
WORKDIR /app

RUN useradd --system --uid 1001 appuser
COPY --from=build /app/app.jar app.jar
USER appuser

EXPOSE 8080
ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75"
ENTRYPOINT ["java", "-jar", "app.jar"]