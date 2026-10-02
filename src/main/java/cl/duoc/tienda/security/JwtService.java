package cl.duoc.tienda.security;
import cl.duoc.tienda.dto.LoginResponse;
import java.time.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

/** Emite el token; la validación posterior la realiza Spring Security. */
@Service
public class JwtService {
    private final JwtEncoder encoder;
    private final String issuer;
    private final long minutos;
    public JwtService(JwtEncoder encoder, @Value("${app.jwt.issuer}") String issuer,
                      @Value("${app.jwt.expiration-minutes}") long minutos) {
        this.encoder = encoder; this.issuer = issuer; this.minutos = minutos;
    }
    public LoginResponse emitir(Authentication auth) {
        Instant ahora = Instant.now();
        String rol = auth.getAuthorities().stream().findFirst()
                .orElseThrow().getAuthority().replaceFirst("^ROLE_", "");
        JwtClaimsSet claims = JwtClaimsSet.builder().issuer(issuer).subject(auth.getName())
                .issuedAt(ahora).expiresAt(ahora.plus(Duration.ofMinutes(minutos)))
                .claim("role", rol).build();
        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        String token = encoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
        return new LoginResponse("Bearer", token, minutos * 60);
    }
}
