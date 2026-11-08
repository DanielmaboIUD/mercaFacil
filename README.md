# Merca Fácil Web

CRUD web con patrón DAO sobre PostgreSQL. Java 21, Servlets, JSP + JSTL, Tomcat 11.

## Requisitos

- JDK 21 y Maven 3.9 o superior.
- Apache Tomcat 11.
- PostgreSQL con la BD `merca_facil` creada con los scripts de `merca_facil/`.
- VS Code con **Extension Pack for Java** y **Community Server Connectors** (opcional).

## Configurar la conexión

Copia la plantilla y escribe tu contraseña:

```
copy src\main\resources\db.properties.example src\main\resources\db.properties
```

`db.properties` no se versiona. La contraseña queda dentro del `.war`, así que al cambiarla hay que recompilar.

## Compilar

```
mvn clean package
```

Genera `target/merca_facil_web.war`.

## Desplegar desde VS Code

1. En el panel **SERVERS**, crea el servidor con *Create New Server* → *No, use server on disk* y selecciona la carpeta de Tomcat.
2. Clic derecho en el servidor → **Add Deployment** → selecciona el `.war`.
3. Inicia el servidor (o usa **Publish Server** si ya estaba corriendo).
4. Abre `http://localhost:8080/merca_facil_web/`.
