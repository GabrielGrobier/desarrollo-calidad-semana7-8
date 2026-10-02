package cl.duoc.tienda.security;

import cl.duoc.tienda.model.CuentaUsuario;
import cl.duoc.tienda.repository.CuentaUsuarioRepository;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.*;
import org.springframework.security.web.SecurityFilterChain;
import java.nio.charset.StandardCharsets;

/** Seguridad stateless: login público, resto de rutas con JWT y roles. */
@Configuration
@EnableMethodSecurity
public class SeguridadConfig {
    @Bean PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    @Bean UserDetailsService userDetailsService(CuentaUsuarioRepository repositorio) {
        return username -> {
            CuentaUsuario u = repositorio.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario inexistente"));
            return User.withUsername(u.getUsername()).password(u.getPasswordHash())
                    .roles(u.getRol().name()).build();
        };
    }

    @Bean AuthenticationManager authenticationManager(UserDetailsService uds, PasswordEncoder encoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(uds);
        provider.setPasswordEncoder(encoder);
        return new ProviderManager(provider);
    }

    @Bean SecretKey jwtSecret(@Value("${app.jwt.secret}") String secreto) {
        byte[] bytes = secreto.getBytes(StandardCharsets.UTF_8);
        if (bytes.length < 32) throw new IllegalArgumentException("El secreto JWT requiere al menos 32 bytes");
        return new SecretKeySpec(bytes, "HmacSHA256");
    }

    @Bean JwtEncoder jwtEncoder(SecretKey secreto) {
        // Firma HS256 de laboratorio; para sistemas reales evaluar claves asimétricas e IdP.
        return new NimbusJwtEncoder(new ImmutableSecret<>(secreto));
    }

    @Bean JwtDecoder jwtDecoder(SecretKey secreto, @Value("${app.jwt.issuer}") String issuer) {
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withSecretKey(secreto)
                .macAlgorithm(MacAlgorithm.HS256).build();
        decoder.setJwtValidator(JwtValidators.createDefaultWithIssuer(issuer));
        return decoder;
    }

    @Bean SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Sin cookies de sesión: el cliente envía Bearer JWT en cada petición.
        http.csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/salud", "/api/auth/login").permitAll()
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/productos", "/api/productos/**")
                    .hasAnyRole("ADMIN", "VENDEDOR", "AUDITOR")
                .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/productos")
                    .hasRole("ADMIN")
                .requestMatchers(org.springframework.http.HttpMethod.PATCH, "/api/productos/**")
                    .hasRole("ADMIN")
                .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/ventas")
                    .hasAnyRole("ADMIN", "VENDEDOR")
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/ventas")
                    .hasAnyRole("ADMIN", "AUDITOR")
                .anyRequest().denyAll())
            .oauth2ResourceServer(oauth -> oauth.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtConverter())))
            .exceptionHandling(errors -> errors
                .authenticationEntryPoint((request, response, exception) -> response.setStatus(HttpStatus.UNAUTHORIZED.value()))
                .accessDeniedHandler((request, response, exception) -> response.setStatus(HttpStatus.FORBIDDEN.value())));
        return http.build();
    }

    private JwtAuthenticationConverter jwtConverter() {
        JwtGrantedAuthoritiesConverter roles = new JwtGrantedAuthoritiesConverter();
        roles.setAuthoritiesClaimName("role");
        roles.setAuthorityPrefix("ROLE_");
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(roles);
        return converter;
    }
}
