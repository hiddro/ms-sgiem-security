FROM openjdk:17
EXPOSE 8080
ADD target/ms-sgiem-security.jar ms-sgiem-security.jar
ENTRYPOINT ["java", "-jar", "/ms-sgiem-security.jar"]