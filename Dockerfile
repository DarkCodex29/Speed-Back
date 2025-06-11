# Dockerfile para SPEED Backend
FROM openjdk:8-jdk-alpine

# Instalar Maven
RUN apk add --no-cache curl tar bash \
    && mkdir -p /usr/share/maven /usr/share/maven/ref \
    && curl -fsSL -o /tmp/apache-maven.tar.gz https://apache.osuosl.org/maven/maven-3/3.9.9/binaries/apache-maven-3.9.9-bin.tar.gz \
    && tar -xzf /tmp/apache-maven.tar.gz -C /usr/share/maven --strip-components=1 \
    && rm -f /tmp/apache-maven.tar.gz \
    && ln -s /usr/share/maven/bin/mvn /usr/bin/mvn

# Establecer directorio de trabajo
WORKDIR /app

# Copiar archivos de configuración Maven
COPY pom.xml .
COPY mvnw .
COPY mvnw.cmd .
COPY .mvn .mvn

# Descargar dependencias (layer caching)
RUN mvn dependency:go-offline -B

# Copiar código fuente
COPY src ./src

# Exponer puerto
EXPOSE 8080

# Comando por defecto
CMD ["mvn", "spring-boot:run"] 