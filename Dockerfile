FROM openjdk:8-jre-alpine

#JDBC_DRIVER=org.postgresql.Driver;JDBC_DATABASE_URL=jdbc:postgresql:todos?user=postgres;SECRET_KEY=898748674728934843;JWT_SECRET=898748674728934843

ENV APPLICATION_USER ktor
ENV JDBC_DRIVER org.postgresql.Driver
ENV JDBC_DATABASE_URL jdbc:postgresql:todos?user=postgres

RUN adduser -D -g '' $APPLICATION_USER

RUN mkdir /app
RUN chown -R $APPLICATION_USER /app

USER $APPLICATION_USER

COPY ./build/libs/todo-app-server-1.0.0-all.jar /app/todo-app-server-1.0.0-all.jar
WORKDIR /app

CMD ["java", "-server", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseCGroupMemoryLimitForHeap", "-XX:InitialRAMFraction=2", "-XX:MinRAMFraction=2", "-XX:MaxRAMFraction=2", "-XX:+UseG1GC", "-XX:MaxGCPauseMillis=100", "-XX:+UseStringDeduplication", "-jar", "todo-app-server-1.0.0-all.jar"]
