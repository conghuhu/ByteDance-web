FROM openjdk:8-jre
ENV DB_URL="jdbc:mysql://localhost:3306/qingxun?autoReconnect=true&useSSL=false&serverTimezone=Asia/Shanghai&useUnicode=true&lowerCaseTableNames=1&characterEncoding=utf8"
ENV DB_USERNAME="root"
ENV DB_PASSWORD="123456"
ENV REDIS_HOST="localhost"
ENV REDIS_PORT="6379"
EXPOSE 8090
ADD ./target/byte_web.jar /app.jar
CMD java -jar /app.jar --spring.profiles.active=prod