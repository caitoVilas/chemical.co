package com.ch.authservice.auth_service.configs.security;

import com.ch.core.chcore.models.UserAuthResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;

/**
 * Componente para la generación de tokens JWT.
 * Utiliza una clave secreta para firmar los tokens.
 * Los tokens incluyen el correo electrónico del usuario y sus roles.
 * La expiración del token está configurada para 1 hora.
 *
 * @author caito
 *
 */
@Component
public class JwtTokenProvider {
    private static final String SECRET = "Hw9z1Yk8Nmq1IzlwcCg8j6yHzw6RKjzZUi9r7Ww555o0PP";

    /**
     * Genera un token JWT para el usuario proporcionado.
     *
     * @param user El usuario para el cual se genera el token.
     * @return El token JWT generado.
     */
    public String generateToken(UserAuthResponse user){
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("roles", this.getRoles(user))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+ 1000 * 60 * 60))
                .signWith(generateKey())
                .compact();
    }

    /**
     * Genera un token de refresco JWT para el usuario proporcionado.
     *
     * @param user El usuario para el cual se genera el token de refresco.
     * @return El token de refresco JWT generado.
     */
    private List<String> getRoles(UserAuthResponse user){
        return user.getRole().stream().map(rol -> rol.getRole().name()).toList();
    }

    /**
     * Genera una clave secreta para firmar los tokens JWT.
     *
     * @return La clave secreta generada.
     */
    private Key generateKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }
}
