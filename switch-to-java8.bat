@echo off
echo ====================================
echo   Cambiando a Java 8 para SPEED
echo ====================================

REM Configurar Java 8 y Maven para esta sesión
set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-8.0.452.9-hotspot
set MAVEN_HOME=C:\apache-maven-3.9.10
set PATH=%JAVA_HOME%\bin;%MAVEN_HOME%\bin;%PATH%

REM Verificar versión
echo.
echo Versión de Java configurada:
java -version

echo.
echo ====================================
echo   Ambiente SPEED listo!
echo ====================================
echo.
echo Comandos disponibles:
echo   .\mvnw.cmd clean compile
echo   .\mvnw.cmd spring-boot:run
echo   .\mvnw.cmd test
echo.

REM Mantener la ventana abierta
cmd /k 