# API REST - Reportes de acumulación de basura

API en **Spring Boot 3.3.5 + PostgreSQL** para que los ciudadanos reporten puntos de
acumulación de basura al área de Limpieza Pública.

- **Group ID:** `com.sandovalpurisaca`
- **Artifact:** `reporteservice`
- **Package:** `com.sandovalpurisaca.reporteservice`
- **Java:** 17
- **Capas:** `entity` → `repository` → `service` → `controller`

---

## 1. Endpoints

| Método | Endpoint | Descripción |
|---|---|---|
| POST | `/reportes/` | Registra un reporte (estado inicial = 1) |
| GET | `/reportes/?offset=0&limit=5` | Lista paginada de reportes |
| GET | `/reportes/{id}` | Obtiene un reporte por su código |
| GET | `/reportes/ciudadano/{dni}` | Todos los reportes de un ciudadano |
| PUT | `/reportes/{id}` | Marca el reporte como atendido (estado 1 → 0) |

### Ejemplos

**Registrar**
```http
POST /reportes/
Content-Type: application/json

{
  "dni": "45871236",
  "fecha": "2026-10-29T10:15:00",
  "tipo": "DOMICILIARIA",
  "direccion": "Av. Los Álamos 123",
  "referencia": "Frente al parque central"
}
```
Respuesta `201 Created`:
```json
{
  "id": 13,
  "dni": "45871236",
  "fecha": "2026-10-29T10:15:00",
  "tipo": "DOMICILIARIA",
  "direccion": "Av. Los Álamos 123",
  "referencia": "Frente al parque central",
  "estado": 1
}
```

**Listar paginado**
```http
GET /reportes/?offset=0&limit=5
```

**Atender**
```http
PUT /reportes/13
```
Respuesta `200 OK` con `"estado": 0`.

**Errores**
- `400` si el DNI no tiene 8 dígitos o falta un campo obligatorio.
- `404` si el id no existe.

---

## 2. Ejecutar en local

### Opción A: PostgreSQL local
1. Crear la base de datos:
   ```sql
   CREATE DATABASE reportesdb;
   ```
2. Ejecutar (las credenciales por defecto son `postgres/postgres`, cámbialas con variables de entorno):
   ```bash
   mvn spring-boot:run
   ```
   o
   ```bash
   mvn clean package -DskipTests
   java -jar target/reporteservice-0.0.1-SNAPSHOT.jar
   ```

### Opción B: apuntar a la base de datos de Render desde tu PC
```bash
DB_URL="jdbc:postgresql://<host-externo>:5432/<base>?sslmode=require" \
DB_USER="<usuario>" \
DB_PASSWORD="<password>" \
mvn spring-boot:run
```
> La conexión **externa** de Render exige `?sslmode=require`. La **interna** (desde el
> propio Web Service) no lo necesita.

La API queda en `http://localhost:8080/reportes/`.
Al primer arranque se crean la tabla `reportes` y 12 registros de ejemplo (solo si está vacía).

### Pruebas automatizadas
```bash
mvn test
```
Usa H2 en memoria y valida los 5 endpoints (7 pruebas).

---

## 3. Despliegue en Render.com

### 3.1 Base de datos
1. Render → **New +** → **Postgres**.
2. Nombre: `reportes-db`, plan **Free**, región la más cercana.
3. Cuando esté lista, copiar de **Connections**:
   - **Hostname** (ej. `dpg-xxxxxxx-a`)
   - **Port** (`5432`)
   - **Database** (`reportes_db` o similar)
   - **Username** y **Password**

### 3.2 Web Service (Docker)
1. Subir este proyecto a GitHub.
2. Render → **New +** → **Web Service** → conectar el repositorio.
3. En **Language/Runtime** elegir **Docker** (detecta el `Dockerfile`).
4. En **Environment Variables** agregar:

   | Variable | Valor |
   |---|---|
   | `DB_URL` | `jdbc:postgresql://<hostname>:5432/<database>` |
   | `DB_USER` | usuario de la base |
   | `DB_PASSWORD` | password de la base |

   > Render entrega la URL como `postgres://usuario:password@host/base`.
   > **No** la pegues tal cual: usa el formato `jdbc:postgresql://...` y separa usuario/password.
   > No escribas credenciales en `application.properties` ni en el repositorio.

5. **Health Check Path:** `/reportes/?offset=0&limit=1`
6. Deploy. La URL queda así:
   `https://reporteservice-sandovalpurisaca.onrender.com/reportes/`

### 3.3 Verificar con Postman
Probar los 5 endpoints contra la URL pública. La primera petición puede tardar
~1 minuto porque el plan gratuito suspende el servicio.

---

## 4. Estructura

```
src/main/java/com/sandovalpurisaca/reporteservice
├── ReporteserviceApplication.java
├── config/DataSeeder.java
├── controller/ReporteController.java
├── entity/Reporte.java
├── exception/GlobalExceptionHandler.java
├── repository/ReporteRepository.java
├── service/ReporteService.java
└── service/impl/ReporteServiceImpl.java
```

---

## 5. Notas de implementación

- **Paginación real:** el repositorio usa `LIMIT :limit OFFSET :offset`; `offset` es
  desplazamiento, no número de página.
- **Estado:** el cliente nunca lo decide. `POST` fuerza `estado = 1` y `PUT` lo cambia a `0`
  (idempotente si ya estaba atendido).
- **Fecha:** formato ISO `yyyy-MM-dd'T'HH:mm:ss`. Si no se envía, el servidor usa la fecha actual.
- **Puerto:** `server.port=${PORT:8080}` para que Render asigne el puerto.
- **Tabla:** Hibernate la crea con `spring.jpa.hibernate.ddl-auto=update`.
