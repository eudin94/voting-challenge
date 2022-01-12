FROM adoptopenjdk/openjdk11
WORKDIR /usr/share/api/
ADD target/*.jar app.jar
CMD ["java","-jar","app.jar"]
