# Merca Fácil Web

Aplicación web para administrar el catálogo, inventario, compras, ventas y pedidos de Merca Fácil. Usa el patrón DAO sobre PostgreSQL con Java, Servlets y JSP + JSTL.

## Requisitos

| Herramienta | Versión |
|---|---|
| JDK | 21 |
| Apache Maven | 3.9 o superior |
| Apache Tomcat | 11.0.x |
| PostgreSQL | Con la BD `merca_facil` disponible |

Verifica el entorno con `java -version` y `mvn -v`.

## Despliegue local

### 1. Clonar el repositorio

```
git clone https://github.com/DanielmaboIUD/mercaFacil.git
cd mercaFacil
```

### 2. Preparar la base de datos

Los scripts de la BD no forman parte de este repositorio; solicita el respaldo `merca_facil_bk.sql` al responsable del proyecto. Para restaurarlo:

```
psql -U postgres -c "CREATE DATABASE merca_facil"
psql -U postgres -d merca_facil -f merca_facil_bk.sql
```

Comprueba que existan las tablas y la vista que usa la aplicación:

```
psql -U postgres -d merca_facil -c "SELECT count(*) FROM articulo"
psql -U postgres -d merca_facil -c "SELECT count(*) FROM v_detalle_ventas"
```

### 3. Configurar la conexión

Crea tu archivo local a partir de la plantilla.

Windows:

```
copy src\main\resources\db.properties.example src\main\resources\db.properties
```

Linux o macOS:

```
cp src/main/resources/db.properties.example src/main/resources/db.properties
```

Edita `src/main/resources/db.properties` con tus datos:

```properties
db.url=jdbc:postgresql://localhost:5432/merca_facil
db.usuario=postgres
db.contrasena=TU_CONTRASENA
```

Este archivo no se versiona. La contraseña queda dentro del `.war`: recompila si la cambias y no compartas ese archivo.

### 4. Compilar

```
mvn clean package
```

Genera `target/merca_facil_web.war`.

### 5. Desplegar en Tomcat

Copia `target/merca_facil_web.war` a la carpeta `webapps` de Tomcat y arráncalo:

- Windows: `bin\startup.bat`
- Linux o macOS: `bin/startup.sh`

Abre <http://localhost:8080/merca_facil_web/>.

**Desde VS Code** (opcional), con las extensiones *Extension Pack for Java* y *Community Server Connectors*:

1. En el panel **SERVERS**, usa *Create New Server* → *No, use server on disk* y selecciona la carpeta de Tomcat.
2. Clic derecho en el servidor → **Add Deployment** → selecciona el `.war`.
3. Inicia el servidor, o usa **Publish Server** si ya estaba corriendo.

## Solución de problemas

| Síntoma | Qué revisar |
|---|---|
| "Usuario o contraseña de la base de datos incorrectos." | `db.usuario` y `db.contrasena`; luego recompila y vuelve a desplegar |
| "La base de datos configurada no existe." | El nombre de la BD en `db.url` |
| "No se pudo conectar con el servidor de PostgreSQL." | Que PostgreSQL esté activo y el host y puerto de `db.url` |
| Error en **Detalle de ventas** | Que exista la vista `v_detalle_ventas` |
| Error 404 | Que la URL incluya `/merca_facil_web/` y que el `.war` esté desplegado |
| Tomcat no arranca | Que `JAVA_HOME` apunte al JDK 21 y que el puerto 8080 esté libre |
