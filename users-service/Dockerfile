# Етап 1: Збірка
FROM maven:3.9.8-eclipse-temurin-17 as builder
WORKDIR /workspace

# Завантаження залежностей (кешування)
COPY pom.xml .
RUN mvn -q dependency:go-offline

# Копіюємо весь код і збираємо
COPY . .
RUN mvn clean package -DskipTests

# Переіменовуємо скомпільований JAR у просту назву
RUN cp target/*.jar application.jar

# Розпакування шарів Spring Boot
RUN java -Djarmode=layertools -jar application.jar extract

# Етап 2: Кінцевий образ
FROM eclipse-temurin:17-jre-alpine
WORKDIR /application

# Копіюємо необхідні файли
COPY --from=builder /workspace/dependencies/ ./dependencies
COPY --from=builder /workspace/snapshot-dependencies/ ./snapshot-dependencies
COPY --from=builder /workspace/spring-boot-loader/ ./spring-boot-loader
COPY --from=builder /workspace/application/ ./application
COPY --from=builder /workspace/application.jar ./application.jar

# Виставляємо порт
EXPOSE 8081

# Запуск
ENTRYPOINT ["java","-jar","application.jar"]