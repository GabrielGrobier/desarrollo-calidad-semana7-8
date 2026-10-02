package cl.duoc.tienda.model;

import jakarta.persistence.*;

/** Cuenta persistida. Nunca almacenamos contraseñas en texto plano. */
@Entity
@Table(name = "cuentas_usuario")
public class CuentaUsuario {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 60)
    private String username;
    @Column(nullable = false)
    private String passwordHash;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Rol rol;

    protected CuentaUsuario() { } // Requisito de JPA para reconstruir la entidad.
    public CuentaUsuario(String username, String passwordHash, Rol rol) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.rol = rol;
    }
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public Rol getRol() { return rol; }
}
