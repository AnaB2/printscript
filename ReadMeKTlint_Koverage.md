Ejecución de ktlint y Kover en el proceso de Build

En este proyecto, hemos configurado ktlint (estilo de código) y Kover (cobertura de pruebas) para ejecutarse automáticamente en el proceso de build. 
Esta configuración se eligió para garantizar que el código cumpla con los estándares de calidad y cobertura antes de ser considerado listo para producción.

Razones por las cuales corremos ktlint y Kover en el proceso de Build:

Consistencia Automática: Al ejecutarse en el build, todas las validaciones se realizan de manera centralizada y automática, asegurando que todos los desarrolladores sigan las mismas reglas sin depender de configuraciones individuales o locales.
Evitar Omisiones: Las validaciones en build no pueden ser ignoradas o accidentalmente omitidas, a diferencia de los Git Hooks (que pueden desactivarse) o las validaciones en CI (que detectan problemas después de los commits).
Retroalimentación Inmediata: Al validar durante el build, los problemas se detectan antes de subir el código, ahorrando tiempo y evitando correcciones tardías.
