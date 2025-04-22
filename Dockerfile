FROM maven:3.8.4-openjdk-17 as builder
LABEL authors="N31"

# Создаем директорию для кэша Maven
RUN mkdir -p /root/.m2/repository

# Копируем только pom.xml для кэширования зависимостей
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline

# Копируем исходный код и собираем проект
COPY . .
RUN mvn -f /app/pom.xml clean package -Dmaven.test.skip=true

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar /app/*.jar
RUN mkdir -p /app/src/main/resources/static
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/*.jar"]