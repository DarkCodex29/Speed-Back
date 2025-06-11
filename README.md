# SPEED - Sistema de Gestión de Contratos 4.0 (Backend)

![Java](https://img.shields.io/badge/Java-8-orange.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-1.5.8-green.svg)
![SQL Server](https://img.shields.io/badge/Database-SQL%20Server-blue.svg)
![Maven](https://img.shields.io/badge/Build-Maven-red.svg)

## 📋 Descripción

Sistema empresarial para gestión completa de contratos y documentos legales con workflows, firma electrónica y reportería.

## 🛠️ Stack Tecnológico

- **Framework:** Spring Boot 1.5.8
- **Lenguaje:** Java 8 (requerido)
- **Base de Datos:** SQL Server (`BDSCA_DEV` + `SPEED_PRD`)
- **Build:** Maven

## ⚙️ Configuración Rápida

### 1. Prerrequisitos
- Java 8 (obligatorio)
- Maven 3.6+
- SQL Server con acceso VPN

### 2. Configuración
```bash
# Configurar Java 8
.\switch-to-java8.bat

# Crear archivo src/main/resources/jdbc.properties
jdbc.datasource.auth.url=jdbc:sqlserver://[IP]:1433;databaseName=BDSCA_DEV
jdbc.datasource.auth.username=[USER]
jdbc.datasource.auth.password=[PASS]

jdbc.datasource.speed.url=jdbc:sqlserver://[IP]:1433;databaseName=SPEED_PRD
jdbc.datasource.speed.username=[USER]
jdbc.datasource.speed.password=[PASS]
```

### 3. Ejecución
```bash
mvn clean compile
mvn spring-boot:run
# Aplicación: http://localhost:9096/services/
```

## 🎯 Para Qué Sirve

**Desarrollar localmente** mientras el frontend corporativo consume tu backend:
- Modificar código y probar inmediatamente
- Debug con breakpoints
- Nuevas funcionalidades sin afectar producción

```javascript
// En frontend: cambiar URL de API
const API_URL = "http://localhost:9096/services/";  // tu backend local
```

## 🔗 APIs Principales

```bash
POST /services/auth                    # Login
POST /services/bandeja/bandejaEntrada  # Bandeja entrada
GET  /services/dashboard/validarAcceso # Dashboard
POST /services/visado/aprobar/{id}     # Aprobar visado
```

## ❓ Problemas Comunes

| Error | Solución |
|-------|----------|
| Java version error | Usar exactamente Java 8 |
| Connection refused | Verificar VPN y credenciales |
| Port 9096 in use | `netstat -ano \| findstr :9096` |

## 🤝 Contribución

1. Fork → 2. Branch → 3. Commit → 4. Push → 5. PR

## 📞 Soporte

Contactar equipo de desarrollo interno para consultas técnicas.

---
**⚡ Listo para desarrollo ⚡**