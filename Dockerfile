FROM openjdk:17-jdk-slim

WORKDIR /app

ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN mkdir /app/dump
RUN chown -R 1000:1000 /app
RUN chmod -R 777 /app

USER 1000

# 设置 JVM 运行时参数，优化性能和内存管理
# -XX:+UseContainerSupport 可以让 JVM 更好地适配容器环境
ENV JAVA_OPTS="-XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/app/dump -XX:+PrintFlagsFinal -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:MinRAMPercentage=20.0"

COPY target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Dspring.profiles.active=$SPRING_PROFILES_ACTIVE -jar app.jar"]
