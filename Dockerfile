FROM openjdk:15-jdk
WORKDIR /home/radio/
COPY build/libs/radio-dashboard-backend-all.jar Radio.jar
ENTRYPOINT java -jar Radio.jar