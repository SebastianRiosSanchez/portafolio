# 🏦 Empresa API

## 🧩 Descripción
Microservicio REST para la gestión de registros de empresas.  
Incluye autenticación con JWT, roles y permisos, validaciones y consultas dinámicas.

## ⚙️ Tecnologías
- Java 17  
- Spring Boot 3  
- Spring Security  
- JPA / Hibernate  
- MySQL  
- Maven  

## 🚀 Funcionalidades principales
- Autenticación con JWT
- CRUD de registros de empresas.
- Filtro por estado `isDeleted`
- Control de roles (SuperAdmin, Admin, User)
- Generación de reportes CSV

## 📡 Endpoints principales
| Método | Endpoint | Descripción |
|--------|-----------|-------------|
| POST | `/api/auth/login` | Iniciar sesión |
| GET | `/api/companies` | Listar empresas |
| POST | `/api/companies` | Crear empresa |

## 🧪 Ejecución local
```bash
git clone https://github.com/sebastianrios/empresa-api.git
cd empresa-api
mvn spring-boot:run
