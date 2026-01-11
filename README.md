#  Sistema de Gestión de Trámites Gubernamentales

Sistema  para la gestión de trámites gubernamentales que permite administrar ciudadanos y sus solicitudes de trámites.


##  Características

-  **Gestión de Ciudadanos**: CRUD completo con validaciones
-  **Gestión de Trámites**: Crear, consultar y cambiar estados
-  **Catálogos**: Tipos de documento y tipos de trámite
-  **Validaciones**: DUI, Pasaporte, NIT con máscaras específicas
-  **Soft Delete**: Los registros no se eliminan físicamente
-  **Auditoría**: Fechas de creación y modificación automáticas
-  **API RESTful**: Documentada con Swagger/OpenAPI
-  **Responsive**: Interfaz adaptable a diferentes dispositivos
---

##  Tecnologías

### Backend
| Tecnología | Versión | Descripción |
|------------|---------|-------------|
| Java | 17 LTS | Lenguaje de programación |
| Spring Boot | 3.4.1 | Framework backend |
| Spring Data JPA | 3.4.1 | Persistencia de datos |
| PostgreSQL | 16 | Base de datos relacional |
| MapStruct | 1.5.5 | Mapeo de DTOs |
| Lombok | - | Reducción de codigo |
| SpringDoc OpenAPI | 2.8.0 | Documentación API |

### Frontend
| Tecnología | Versión | Descripción |
|------------|---------|-------------|
| Angular | 17.3 | Framework frontend |
| PrimeNG | 17.18 | Componentes UI |
| PrimeFlex | 4.0 | Utilidades CSS |
| TypeScript | 5.4 | Lenguaje tipado |
| RxJS | 7.8 | Programación reactiva |

### DevOps
| Tecnología | Descripción |
|------------|-------------|
| Docker | Contenedores |
| Docker Compose | Orquestación |
| Nginx | Servidor web frontend |

---

##  Requisitos Previos

### Para Docker (Recomendado)
- [Docker Desktop](https://www.docker.com/products/docker-desktop/) 20.10+
- [Docker Compose](https://docs.docker.com/compose/) 2.0+

### Para Ejecución Manual
- [Java JDK 17]
- [Node.js 20 LTS](https://nodejs.org/)
- [PostgreSQL 16](https://www.postgresql.org/download/)
- [Maven 3.9+](https://maven.apache.org/) 

---

##  Instalación y Ejecución

### Clonar el Repositorio

```bash
git clone https://github.com/WilliamV10/PruebaTecnicaMined.git
cd PruebaTecnicaMined
```

---

### Opción 1: Docker 

La forma más sencilla de ejecutar todo el sistema.

#### Paso 1: Levantar los servicios

```bash
docker-compose up -d --build
```

Este comando:
-  Construye las imágenes de frontend y backend
-  Crea la base de datos PostgreSQL
-  Ejecuta los scripts SQL automáticamente
-  Configura el proxy reverso con Nginx

#### Paso 2: Verificar que todo esté corriendo

```bash
docker-compose ps
```

Deberías ver:
```
NAME                STATUS              PORTS
tramites-db         Up (healthy)        0.0.0.0:5433->5432/tcp
tramites-backend    Up (healthy)        0.0.0.0:8081->8081/tcp
tramites-frontend   Up (healthy)        0.0.0.0:4200->80/tcp
```

#### Paso 3: Acceder a la aplicación

| Servicio | URL |
|----------|-----|
|  **Frontend** | http://localhost:4200 |
|  **Backend API** | http://localhost:8081/api/v1 |
|  **Swagger UI** | http://localhost:8081/swagger-ui.html |
|  **PostgreSQL** | localhost:5433 |


### Opción 2: Ejecución Manual

Si prefieres ejecutar cada componente por separado.

#### Paso 1: Configurar la Base de Datos

1. **Crear la base de datos**
```sql
CREATE DATABASE tramites;
```

2. **Ejecutar el script de creación**
```bash
psql -U postgres -d tramites -f sql/scriptCreacion.sql
```

O usando pgAdmin, ejecuta el contenido del archivo `sql/scriptCreacion.sql`.

#### Paso 2: Configurar y Ejecutar el Backend

1. **Configurar la conexión a BD** 

Edita `tramites/src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/tramites
spring.datasource.username=postgres
spring.datasource.password=TU_PASSWORD
```

2. **Ejecutar el backend**

**Windows:**
```bash
cd tramites
.\mvnw spring-boot:run
```

El backend estará disponible en: http://localhost:8081

#### Paso 3: Configurar y Ejecutar el Frontend

1. **Instalar dependencias**
```bash
cd tramites-frontend
npm install
```

2. **Ejecutar en modo desarrollo**
```bash
npm start
```

El frontend estará disponible en: http://localhost:4200


## API Documentation

La documentación completa de la API está disponible en Swagger UI:

**URL:** http://localhost:8081/swagger-ui.html

### Endpoints Principales

#### Ciudadanos
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/v1/ciudadanos` | Listar todos los ciudadanos |
| GET | `/api/v1/ciudadanos/{id}` | Obtener ciudadano por ID |
| POST | `/api/v1/ciudadanos` | Crear nuevo ciudadano |
| PUT | `/api/v1/ciudadanos/{id}` | Actualizar ciudadano |
| DELETE | `/api/v1/ciudadanos/{id}` | Eliminar ciudadano (soft delete) |

#### Trámites
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/v1/tramites` | Listar todos los trámites |
| GET | `/api/v1/tramites/{id}` | Obtener trámite por ID |
| POST | `/api/v1/tramites` | Crear nuevo trámite |
| PATCH | `/api/v1/tramites/{id}/estado` | Cambiar estado del trámite |
| DELETE | `/api/v1/tramites/{id}` | Eliminar trámite (soft delete) |

#### Catálogos
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/v1/tipos-documento` | Listar tipos de documento |
| GET | `/api/v1/tipos-tramite` | Listar tipos de trámite |

### Credenciales por Defecto (Docker)

| Parámetro | Valor |
|-----------|-------|
| Host | localhost |
| Puerto | 5433 |
| Base de datos | tramites |
| Usuario | postgres |
| Contraseña | postgres123 |


## 🔧 Configuración Adicional

### Variables de Entorno (Backend)

| Variable | Valor por Defecto | Descripción |
|----------|-------------------|-------------|
| `SPRING_DATASOURCE_URL` | jdbc:postgresql://postgres:5432/tramites | URL de conexión |
| `SPRING_DATASOURCE_USERNAME` | postgres | Usuario BD |
| `SPRING_DATASOURCE_PASSWORD` | postgres123 | Contraseña BD |
| `SERVER_PORT` | 8081 | Puerto del servidor |

### Cambiar Puerto del Frontend

Edita `docker-compose.yml`:
```yaml
frontend:
  ports:
    - "3000:80"  # Cambia 4200 por el puerto deseado
```

---

## Solución de Problemas

### El backend no conecta a la base de datos

```bash
# Verificar que PostgreSQL esté corriendo
docker-compose ps

# Ver logs de PostgreSQL
docker-compose logs postgres
```

### Error de puertos en uso

```bash
# Verificar qué está usando el puerto
netstat -ano | findstr :8081
netstat -ano | findstr :4200

# Cambiar puertos en docker-compose.yml si es necesario
```

### Limpiar todo y empezar de cero

```bash
docker-compose down -v --rmi all
docker-compose up -d --build
```

---
