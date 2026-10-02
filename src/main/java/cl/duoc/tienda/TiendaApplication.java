package cl.duoc.tienda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** Punto de entrada. @SpringBootApplication descubre controllers, services y repositorios. */
@SpringBootApplication
public class TiendaApplication {
    public static void main(String[] args) {
        SpringApplication.run(TiendaApplication.class, args);
    }
}
