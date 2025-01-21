# ForoHub

ForoHub es una réplica de un foro a nivel de back end y para eso crearemos una API REST usando Spring. Este proyecto está construido con Spring Boot y utiliza JWT para la autenticación.

## Características

- Registro y autenticación de usuarios
- Creación, lectura, actualización y eliminación (CRUD) de tópicos
- Seguridad basada en JWT
- Gestión de roles y permisos

## Tecnologías Utilizadas

- Java 17
- Spring Boot 2.7.x
- Spring Security
- JWT (JSON Web Tokens)
- JPA/Hibernate
- MySQL (como gestor de base de datos)
- H2 Database (para desarrollo y pruebas)
- Lombok
- Maven

## Requisitos Previos

- JDK 17 o superior
- Maven 3.6 o superior
- MySQL

## Configuración del Proyecto

### Clonar el Repositorio

bash
- git clone https://github.com/rhizor/Forohub.git
- cd Forohub

## Configuración de la Base de Datos

Asegúrate de tener una instancia de MySQL en ejecución y crea una base de datos para el proyecto. Luego, configura las credenciales de la base de datos en el archivo application.properties:

- spring.datasource.url=jdbc:mysql://localhost:3306/forohub
- spring.datasource.username=tu_usuario
- spring.datasource.password=tu_contrasena
- spring.jpa.hibernate.ddl-auto=update
- spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

## Construir el Proyecto
-mvn clean install

## Ejecutar la Aplicación
- abre la terminal detu IDE
- mvn spring-boot:run
La aplicación estará disponible en http://localhost:8080

## Estado Actual del Proyecto

- Actualmente están implementadas todas las funcionalidades, sin embargo, hasta el momento solo se ha podido verificar la funcionalidad registrar el usuario. Al intentar hacer login, se produce un error de StackOverflowError. Este problema se estará corrigiendo en el transcurso de los días para terminar de verificar las demás funcionalidades que se requieren despues del login.

## Endpoints Principales
### Autenticación

- POST /api/auth/register: Registro de nuevos usuarios
- POST /api/auth/login: Autenticación de usuarios

### Tópicos

- GET /api/topics: Obtener todos los tópicos
- POST /api/topics: Crear un nuevo tópico
- GET /api/topics/{id}: Obtener un tópico por ID
- PUT /api/topics/{id}: Actualizar un tópico por ID
- DELETE /api/topics/{id}: Eliminar un tópico por ID

## Ejemplos de Solicitudes
### Registro de Usuarios
curl -X POST http://localhost:8080/api/auth/register -H "Content-Type: application/json" -d '{
  "nombre": "Juan Perez",
  "correoElectronico": "juan.perez@example.com",
  "contrasena": "password123"
}'

### Autenticación de Usuarios
curl -X POST http://localhost:8080/api/auth/login -H "Content-Type: application/json" -d '{
  "username": "juan.perez@example.com",
  "password": "password123"
}'

### Crear un Tópico
curl -X POST http://localhost:8080/api/topics -H "Content-Type: application/json" -H "Authorization: Bearer <TOKEN>" -d '{
  "titulo": "Nuevo Tópico",
  "contenido": "Contenido del tópico"
}'

## Contribuir

¡Las contribuciones son bienvenidas! Para contribuir, por favor sigue estos pasos:

1. Haz un fork del proyecto
2. Crea una nueva rama (git checkout -b feature/nueva-caracteristica)
3. Realiza tus cambios y haz commit (git commit -am 'Añadir nueva característica')
4. Haz push a la rama (git push origin feature/nueva-caracteristica)
5. Abre un Pull Request

## Licencia

Este proyecto está bajo la Licencia MIT. Consulta el archivo LICENSE para más detalles.

## Contacto

Para cualquier pregunta o sugerencia, por favor abre un issue en GitHub o contacta con el mantenedor del proyecto.

