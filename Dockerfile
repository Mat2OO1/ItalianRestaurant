FROM maven AS maven

WORKDIR /app

COPY pom.xml ./
RUN mvn dependency:go-offline

COPY src ./src

CMD ["mvn", "spring-boot:run"]
