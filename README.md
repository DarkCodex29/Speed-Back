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

### Prerrequisitos
- Java 8+
- Maven 3.6+
- SQL Server 2016+
- Alfresco 5.0+ (opcional)

### Variables de Entorno
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

### Instalación

1. **Clonar el repositorio**
   ```bash
   git clone https://github.com/tu-usuario/speed-back-feature-merge-project.git
   cd speed-back-feature-merge-project
   ```

2. **Configurar base de datos**
   - Copiar `src/main/resources/jdbc.properties.example` como `jdbc.properties`
   - Actualizar las credenciales reales en `jdbc.properties`
   - Ejecutar scripts de base de datos (si están disponibles)

3. **Instalar dependencias**
   ```bash
   mvn clean install
   ```

4. **Ejecutar aplicación**
   ```bash
   mvn spring-boot:run
   ```

5. **Verificar instalación**
   ```
   http://localhost:8080/services/health
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

### Perfiles de Ejecución
```bash
# Desarrollo
mvn spring-boot:run -Dspring.profiles.active=dev

# Producción  
mvn spring-boot:run -Dspring.profiles.active=prod

# Testing
mvn spring-boot:run -Dspring.profiles.active=test
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
- Usar los archivos `.example` como plantillas
- Configurar variables de entorno para producción
- Mantener el `.gitignore` actualizado

### 📋 Archivos Protegidos
```
src/main/resources/jdbc.properties       # Credenciales de BD
src/main/resources/ldap.properties       # Configuración LDAP
src/main/resources/mail.properties       # Credenciales de email
src/main/resources/alfresco.properties   # URLs y tokens de Alfresco
```

### 🔐 Variables de Entorno Recomendadas
```bash
export DB_AUTH_URL="jdbc:sqlserver://server;databaseName=auth_db"
export DB_AUTH_USER="db_user"
export DB_AUTH_PASSWORD="secure_password"
export JWT_SECRET="your_super_secure_jwt_secret"
export FRONTEND_URL="https://your-frontend-domain.com"
```

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

**Desarrollado con ❤️ por el Equipo de SPEED - Hochschild Mining** #   S p e e d - B a c k  
 