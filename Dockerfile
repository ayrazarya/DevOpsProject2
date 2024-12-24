FROM openjdk:17
EXPOSE 8080
ADD target /com.devops.jar
ENTRYPOINT ["java","-jar","/com.devops.jar"]
