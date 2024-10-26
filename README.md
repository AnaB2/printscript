
# Kotlin Project with Gradle - Build, Test, Format, and Publish

Este proyecto utiliza **Gradle** para la automatización de compilación, pruebas, formateo de código y publicación local. A continuación se presentan los comandos necesarios para realizar estas tareas.

## 1. Compilar el Proyecto

Para compilar el proyecto y generar los artefactos en el directorio `build/`, utiliza el siguiente comando:

```bash
./gradlew build
```

## 2. Ejecutar Todos los Tests

Para ejecutar todos los tests del proyecto y generar un reporte de resultados en `build/reports/tests/test/index.html`:

```bash
./gradlew test
```

## 3. Formatear el Código con Ktlint

### Configuración de Ktlint

Asegúrate de que el plugin de **ktlint** esté incluido en tu archivo `build.gradle`.

#### Para `build.gradle` (Groovy DSL):

```groovy
plugins {
    id "org.jlleitschuh.gradle.ktlint" version "11.0.0"
}
```

#### Para `build.gradle.kts` (Kotlin DSL):

```kotlin
plugins {
    id("org.jlleitschuh.gradle.ktlint") version "11.0.0"
}
```

### Comandos para Formateo de Código

#### Formatear el código automáticamente con ktlint:
```bash
./gradlew ktlintFormat
```

#### Verificar el estilo de código sin aplicar correcciones:
```bash
./gradlew ktlintCheck
```

## 4. Publicar el Paquete Localmente con Gradle

Si deseas publicar el artefacto en tu repositorio local de Maven (`~/.m2/repository`) para ser utilizado por otros proyectos en tu máquina, utiliza:

```bash
./gradlew publishToMavenLocal
```

## 5. Hacer un Clean Build

Para limpiar los archivos generados anteriormente y realizar una compilación completa desde cero:

```bash
./gradlew clean build
```

Este comando limpiará el directorio `build/` y luego compilará todo el proyecto nuevamente.

## 6. Ejecutar Hooks (opcional)

Si tienes hooks configurados para **pre-commit** o cualquier otro tipo de hook de git, puedes ejecutar manualmente la verificación de formato con:

```bash
./gradlew ktlintCheck
```

Asegúrate de que tus hooks estén configurados en la carpeta `.git/hooks` o mediante herramientas como **pre-commit**.
