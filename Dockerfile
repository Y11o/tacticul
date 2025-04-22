FROM maven:3.8.4-openjdk-17 as builder
LABEL authors="N31"

# Создаем директорию для кэша Maven
RUN mkdir -p /root/.m2/repository

# Копируем только pom.xml для кэширования зависимостей
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline ${MAVEN_OPTS}

# Копируем исходный код и собираем проект
COPY . .
RUN mvn -f /app/pom.xml clean package -Dmaven.test.skip=true ${MAVEN_OPTS}

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar /app/*.jar

# Установка Python и зависимостей
RUN apk add --no-cache python3 py3-pip && \
    pip3 install requests

# Создаем директории для статических файлов и скриптов
RUN mkdir -p /app/static && \
    mkdir -p /app/scripts

# Копируем скрипт
COPY resetpassword.py /app/scripts/
RUN chmod +x /app/scripts/resetpassword.py

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/*.jar"]