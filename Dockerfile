FROM eclipse-temurin:19-jdk-jammy as base

# 设置工作目录
WORKDIR /app

COPY . .

# 打包
RUN ./gradlew build

COPY build/libs/*.jar app.jar

# 复制密码文件到工作目录
COPY password.txt password.txt

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/app.jar"]