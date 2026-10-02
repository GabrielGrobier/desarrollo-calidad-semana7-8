# Imagen oficial MySQL LTS. Este Dockerfile corresponde a la BASE DE DATOS.
FROM mysql:8.4
# MySQL ejecuta los *.sql de esta carpeta solamente al inicializar un volumen vacío.
COPY init/01-laboratorio.sql /docker-entrypoint-initdb.d/01-laboratorio.sql
