# Hackathon DWI

Se establece lo siguiente:

- Luego de cada evaluación, la plataforma UTP+ class proporciona un reporte de
  las notas registradas.
- La plataforma no brinda un detalle de las calificaciones por cada pregunta.
- El docente requiere la información detallada por cada pregunta para conocer
  con más detalle el nivel de aprovechamiento de cada estudiante con los temas
  evaluados.
- Se requiere una aplicación que permita al docente ingresar los puntos
  obtenidos por cada una de las preguntas de un examen. Este ingreso se puede
  realizar por importación de archivo o ingreso manual.
- La aplicación generara un reporte detallado con la información proporcionada.
  El reporte debe incluir:
  - Un gráfico de pastel que muestre el numero de aprobados y desaprobados.
  - Un gráfico de barras con el número de estudiantes por cada nota (de 0 a 20).
  - Un podio con los tres primeros puestos (considerar empates).
  - Un gráfico estadístico de barras con el número de alumnos por puntaje para
    cada pregunta de la evaluación.

## Tecnologías

- Java 21
- Jakarta EE (integrado con Spring Boot)
- JSF
- Spring Boot
- Spring Data JPA
- Spring Security

### Especificaciones técnicas

- La interfaz debe estar construida usando JSF y Facelets tags.
- La aplicación debe tener soporte para idioma español e inglés.
- La aplicación consume los datos a través de un servicio REST.
- Toda la información debe ser almacenada en una base de datos.
