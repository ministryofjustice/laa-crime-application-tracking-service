FROM amazoncorretto:21-alpine
RUN mkdir -p /opt/laa-crime-application-tracking-service/
WORKDIR /opt/laa-crime-application-tracking-service/
COPY ./build/libs/crime-application-tracking.jar /opt/laa-crime-application-tracking-service/app.jar
RUN addgroup -S appgroup && adduser -u 1001 -S appuser -G appgroup
USER 1001
EXPOSE 8490 8500
ENTRYPOINT ["java","-jar","app.jar"]
