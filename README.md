# SPEED - Sistema de Gestión de Contratos 4.0 (Backend)

![Java](https://img.shields.io/badge/Java-8-orange.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-1.5.8-green.svg)
![SQL Server](https://img.shields.io/badge/Database-SQL%20Server-blue.svg)
![Maven](https://img.shields.io/badge/Build-Maven-red.svg)

## 📋 Descripción

**SPEED** (Sistema de Gestión de Contratos 4.0) es una aplicación empresarial robusta diseñada para gestionar el ciclo completo de contratos y documentos legales. El sistema proporciona funcionalidades avanzadas de workflow, firma electrónica, gestión documental y reportería.

## 🚀 Características Principales

- ✅ **Gestión de Expedientes** y Documentos Legales
- ✅ **Workflows de Aprobación** con sistema de visados
- ✅ **Firma Electrónica** integrada
- ✅ **Bandeja de Tareas** personalizada por perfil
- ✅ **Dashboard** con métricas en tiempo real
- ✅ **Sistema de Alertas** y notificaciones automáticas
- ✅ **Reportería Avanzada** con exportación a Excel/PDF
- ✅ **Gestión Documental** integrada con Alfresco
- ✅ **Mantenimiento** de catálogos y configuraciones
- ✅ **Integración LDAP** para autenticación corporativa

## 🛠️ Stack Tecnológico

### Backend
- **Framework:** Spring Boot 1.5.8
- **Lenguaje:** Java 8
- **Seguridad:** Spring Security + JWT
- **ORM:** Spring Data JPA + Hibernate
- **Build:** Maven

### Base de Datos
- **Motor:** Microsoft SQL Server
- **Esquemas:** BDSCA_DEV (Auth) + SPEED_PRD_VAL (Principal)

### Integraciones
- **CMS:** Alfresco 5.0
- **Firma Digital:** API de Firma Electrónica
- **Servicios Web:** Apache Axis2 (SOAP)
- **HTTP Client:** OkHttp3
- **Reportes:** Apache POI + JXLS

## 📁 Estructura del Proyecto

```
src/main/java/com/hochschild/speed/back/
├── 📂 config/              # Configuraciones de Spring
├── 📂 controller/          # Controladores REST (50+)
│   ├── 📂 mantenimiento/   # APIs de mantenimiento
│   └── 📂 mobile/          # APIs para móvil
├── 📂 model/               # Modelos de datos
│   ├── 📂 domain/          # Entidades JPA
│   ├── 📂 bean/            # DTOs por funcionalidad
│   ├── 📂 filter/          # Filtros de búsqueda
│   └── 📂 response/        # Respuestas de API
├── 📂 service/             # Servicios de negocio (80+)
├── 📂 repository/          # Repositorios de datos
├── 📂 security/            # Configuración de seguridad
├── 📂 util/                # Utilidades y excepciones
└── 📂 ws/                  # Clientes de servicios web
```

## ⚙️ Configuración e Instalación

### 📋 Prerrequisitos
- **Java 8** (OBLIGATORIO - el proyecto NO funciona con versiones superiores)
- **Maven 3.6+** 
- **SQL Server 2016+**
- **Alfresco 5.0+** (opcional)

⚠️ **IMPORTANTE:** Este proyecto requiere específicamente Java 8 debido a dependencias legacy (Spring Boot 1.5.8, Lombok 1.16.8).

### 🛠️ Configuración de Entorno

#### **Windows - Configuración Automática (Recomendado):**
1. **Instalar Java 8**
   - Descargar desde: https://adoptium.net/es/temurin/releases/?version=8
   - Instalar normalmente (puede coexistir con otras versiones de Java)

2. **Usar script automático**
   ```bash
   .\switch-to-java8.bat
   ```
   Este script configura automáticamente Java 8 y Maven para la sesión actual.

#### **Configuración Manual:**
```properties
# Variables de Entorno (si no usas el script)
JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-8.0.452.9-hotspot
MAVEN_HOME=C:\apache-maven-3.9.10
PATH=%JAVA_HOME%\bin;%MAVEN_HOME%\bin;%PATH%
```

### 📊 Variables de Entorno
```properties
# Base de Datos
DB_AUTH_URL=jdbc:sqlserver://localhost;databaseName=AUTH_DB
DB_AUTH_USER=db_user
DB_AUTH_PASSWORD=your_secure_password

DB_SPEED_URL=jdbc:sqlserver://localhost;databaseName=SPEED_DB
DB_SPEED_USER=db_user
DB_SPEED_PASSWORD=your_secure_password

# JWT
JWT_SECRET=your_jwt_secret_key

# URLs
FRONTEND_URL=https://your-frontend-url
ALFRESCO_URL=https://your-alfresco-url
```

### 🚀 Instalación

1. **Clonar el repositorio**
   ```bash
   git clone https://github.com/tu-usuario/speed-back-feature-merge-project.git
   cd speed-back-feature-merge-project
   ```

2. **Configurar base de datos**
   - Actualizar `src/main/resources/jdbc.properties` con credenciales reales
   - Ejecutar scripts de base de datos (si están disponibles)

3. **Compilar proyecto**
   ```bash
   # Opción 1: Usando script automático (Recomendado)
   .\switch-to-java8.bat
   # En la nueva ventana:
   mvn clean compile
   
   # Opción 2: Maven directo (si tienes Java 8 configurado)
   mvn clean compile
   ```

4. **Ejecutar aplicación**
   ```bash
   mvn spring-boot:run
   ```

5. **Verificar instalación**
   ```
   http://localhost:8080/services/health
   ```

### ⚠️ Comandos Correctos

```bash
# ✅ Comandos que SÍ funcionan:
mvn clean compile        # Compilar proyecto
mvn spring-boot:run      # Ejecutar aplicación  
mvn test                # Ejecutar tests
mvn package             # Crear WAR

# ❌ Comandos que NO existen:
mvn run                 # NO EXISTE
.\mvnw.cmd              # Maven Wrapper está roto
```

### 🔍 Estado del Proyecto

✅ **Funcional:**
- Compila correctamente (820 archivos)
- Ejecuta sin errores
- Todas las dependencias se resuelven

⚠️ **Warnings conocidos (no críticos):**
- APIs internas de Java en `EncryptUtil.java`
- Dependencias duplicadas en `pom.xml`
- Maven Wrapper requiere reparación

🛠️ **Para desarrollo use:**
```bash
.\switch-to-java8.bat    # Configurar entorno automáticamente
mvn spring-boot:run      # Ejecutar para desarrollo
```

## 🔗 Endpoints Principales

### Autenticación
```http
POST /services/auth/login          # Iniciar sesión
POST /services/auth/refresh        # Renovar token
GET  /services/auth/profile        # Perfil de usuario
```

### Gestión de Expedientes
```http
GET    /services/expediente/bandeja    # Bandeja de entrada
POST   /services/expediente/registrar  # Crear expediente
PUT    /services/expediente/{id}       # Actualizar expediente
DELETE /services/expediente/{id}       # Archivar expediente
```

### Documentos
```http
GET    /services/documento/buscar      # Buscar documentos
POST   /services/documento/adjuntar    # Adjuntar archivo
POST   /services/documento/firmar      # Enviar a firma
GET    /services/documento/{id}        # Descargar documento
```

### Reportes
```http
GET /services/reporte/dashboard        # Métricas del dashboard
GET /services/reporte/excel           # Exportar a Excel
GET /services/reporte/seguimiento     # Reporte de seguimiento
```

## 🔐 Seguridad

### Autenticación JWT
- **Header:** `Authorization: Bearer <token>`
- **Expiración:** Configurable en propiedades
- **Refresh:** Endpoint dedicado para renovación

### Roles y Permisos
- **ADMIN:** Acceso completo al sistema
- **ABOGADO:** Gestión de documentos legales
- **USUARIO:** Operaciones básicas
- **CONSULTA:** Solo lectura

### CORS
Configurado para permitir requests desde el frontend configurado en las variables de entorno.

## 📊 Métricas del Proyecto

- **Controladores:** 50+ endpoints REST
- **Servicios:** 80+ servicios de negocio
- **Entidades:** 100+ modelos de dominio
- **Integraciones:** 5+ sistemas externos
- **Líneas de código:** 50,000+ LOC

## 🔧 Configuración Avanzada

### 🚀 Comandos de Desarrollo

#### **Configuración inicial (una sola vez):**
```bash
# Windows - Configurar entorno automáticamente
.\switch-to-java8.bat
```

#### **Comandos principales:**
```bash
# Compilar proyecto
mvn clean compile

# Ejecutar aplicación (desarrollo)
mvn spring-boot:run

# Ejecutar tests
mvn test

# Crear WAR para despliegue
mvn package
```

#### **Perfiles de Ejecución:**
```bash
# Desarrollo
mvn spring-boot:run -Dspring.profiles.active=dev

# Producción  
mvn spring-boot:run -Dspring.profiles.active=prod

# Testing
mvn spring-boot:run -Dspring.profiles.active=test
```

#### **❌ Comandos que NO funcionan:**
```bash
mvn run                 # NO EXISTE - usar mvn spring-boot:run
.\mvnw.cmd             # Maven Wrapper está roto - usar mvn directo
```

### Propiedades Principales
```properties
# Servidor
server.port=${PORT:8080}
server.context-path=/services

# JPA
spring.jpa.show-sql=false
spring.jpa.hibernate.ddl-auto=validate

# Upload
spring.servlet.multipart.max-file-size=30MB
spring.servlet.multipart.max-request-size=30MB
```

## 📈 Monitoreo y Logs

### Endpoints de Salud
```http
GET /services/actuator/health      # Estado de la aplicación
GET /services/actuator/info        # Información del sistema
GET /services/actuator/metrics     # Métricas de rendimiento
```

### Configuración de Logs
```properties
# Nivel de logs
logging.level.com.hochschild.speed=DEBUG
logging.level.org.springframework=INFO
logging.level.org.hibernate.SQL=DEBUG
```

## 🧪 Testing

```bash
# Ejecutar todos los tests
mvn test

# Tests de integración
mvn verify

# Cobertura de código
mvn jacoco:report
```

## 📚 Documentación Adicional

- **API Docs:** Disponible en `/services/swagger-ui.html` (pendiente)
- **Base de Datos:** Diagrama ER en `/docs/database/`
- **Workflows:** Diagramas de proceso en `/docs/business/`

## 🤝 Contribución

1. Fork el proyecto
2. Crear una rama feature (`git checkout -b feature/nueva-funcionalidad`)
3. Commit los cambios (`git commit -am 'Agregar nueva funcionalidad'`)
4. Push a la rama (`git push origin feature/nueva-funcionalidad`)
5. Crear un Pull Request

## 📝 Convenciones de Código

- **Estilo:** Google Java Style Guide
- **Naming:** CamelCase para clases, camelCase para métodos
- **Comentarios:** JavaDoc para APIs públicas
- **Tests:** Nomenclatura `should_ReturnExpected_When_GivenInput`

## 🔒 Seguridad y Configuración

### ⚠️ Archivos Sensibles
- **NUNCA** commitear archivos con credenciales reales
- Configurar variables de entorno para producción
- El `.gitignore` ya está configurado para proteger archivos sensibles

### 📋 Archivos Protegidos (por .gitignore)
```
src/main/resources/jdbc.properties       # Credenciales de BD
src/main/resources/ldap.properties       # Configuración LDAP  
src/main/resources/mail.properties       # Credenciales de email
src/main/resources/alfresco.properties   # URLs y tokens de Alfresco
local-repo/                             # Dependencias locales
*.key, *.crt, *.p12                     # Certificados
application-*.properties                # Configuraciones por ambiente
```

### 🔐 Variables de Entorno Recomendadas
```bash
export DB_AUTH_URL="jdbc:sqlserver://server;databaseName=auth_db"
export DB_AUTH_USER="db_user"
export DB_AUTH_PASSWORD="secure_password"
export JWT_SECRET="your_super_secure_jwt_secret"
export FRONTEND_URL="https://your-frontend-domain.com"
```

### 🚨 Warnings Conocidos del Proyecto

#### **APIs Internas de Java (No crítico):**
```
[WARNING] com.sun.org.apache.xerces.internal.impl.dv.util.Base64 
is internal proprietary API and may be removed in a future release
```
**Ubicación:** `src/main/java/com/hochschild/speed/back/util/EncryptUtil.java`  
**Impacto:** Bajo - funciona en Java 8, puede requerir cambio en versiones futuras

#### **Dependencias Duplicadas (No crítico):**
```
[WARNING] 'dependencies.dependency.(groupId:artifactId:type:classifier)' must be unique:
org.apache.poi:poi:jar -> version 5.2.3 vs ${org.apache.poi-version}
net.sf.jxls:jxls-core:jar -> version 1.0.5 vs 1.0.6
```
**Ubicación:** `pom.xml` líneas 250 y 265  
**Impacto:** Bajo - Maven usa la primera declaración encontrada

## ⚠️ Notas Importantes

### Versiones Legacy
- **Spring Boot 1.5.8** - Considerar migración a 3.x
- **Java 8** - Actualizar a Java 11+ recomendado
- **Dependencias** - Revisar vulnerabilidades de seguridad

### Roadmap de Modernización
- [ ] Migración a Spring Boot 3.x
- [ ] Actualización a Java 17
- [ ] Implementación de OpenAPI 3.0
- [ ] Containerización con Docker
- [ ] CI/CD con GitHub Actions

## 📞 Soporte

- **Equipo de Desarrollo:** [equipo-speed@hochschild.com](mailto:equipo-speed@hochschild.com)
- **Issues:** Usar el sistema de issues de GitHub
- **Wiki:** Documentación técnica en el Wiki del proyecto

## 📄 Licencia

Proyecto propietario de Hochschild Mining - Todos los derechos reservados.

---

**Desarrollado con ❤️ por el Equipo de SPEED - Hochschild Mining** #   S p e e d - B a c k 
 
 