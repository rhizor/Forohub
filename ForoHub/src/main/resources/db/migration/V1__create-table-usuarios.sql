-- src/main/resources/db/migration/V1__create-table-usuarios.sql
CREATE TABLE usuarios (
                          id BIGINT NOT NULL AUTO_INCREMENT,
                          nombre VARCHAR(100) NOT NULL,
                          email VARCHAR(100) NOT NULL UNIQUE,
                          password VARCHAR(300) NOT NULL,
                          activo TINYINT DEFAULT 1,

                          PRIMARY KEY(id)
);

-- src/main/resources/db/migration/V2__create-table-cursos.sql
CREATE TABLE cursos (
                        id BIGINT NOT NULL AUTO_INCREMENT,
                        nombre VARCHAR(100) NOT NULL,
                        categoria VARCHAR(100) NOT NULL,
                        activo TINYINT DEFAULT 1,

                        PRIMARY KEY(id)
);

-- src/main/resources/db/migration/V3__create-table-topicos.sql
CREATE TABLE topicos (
                         id BIGINT NOT NULL AUTO_INCREMENT,
                         titulo VARCHAR(100) NOT NULL,
                         mensaje TEXT NOT NULL,
                         fecha_creacion DATETIME NOT NULL,
                         status VARCHAR(20) NOT NULL,
                         autor_id BIGINT NOT NULL,
                         curso_id BIGINT NOT NULL,
                         activo TINYINT DEFAULT 1,

                         PRIMARY KEY(id),
                         FOREIGN KEY(autor_id) REFERENCES usuarios(id),
                         FOREIGN KEY(curso_id) REFERENCES cursos(id)
);

-- src/main/resources/db/migration/V4__insert-usuarios-cursos.sql
INSERT INTO usuarios (nombre, email, password) VALUES
                                                   ('Admin', 'admin@mail.com', '$admin'),
                                                   ('Usuario', 'usuario@mail.com', '$usuario');

INSERT INTO cursos (nombre, categoria) VALUES
                                           ('Spring Boot 3', 'Programación'),
                                           ('Java OOP', 'Programación'),
                                           ('MySQL', 'Base de datos');

-- src/main/resources/db/migration/V5__insert-topicos.sql
INSERT INTO topicos (titulo, mensaje, fecha_creacion, status, autor_id, curso_id) VALUES
                                                                                      ('Duda sobre JPA', '¿Cómo mapear relaciones Many-to-Many?', NOW(), 'NO_RESPONDIDO', 1, 1),
                                                                                      ('Error en Controller', 'No puedo inyectar el servicio', NOW(), 'NO_RESPONDIDO', 2, 1);