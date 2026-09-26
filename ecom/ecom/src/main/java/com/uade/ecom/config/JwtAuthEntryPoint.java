package com.uade.ecom.config;

import java.io.IOException;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Que responde la API cuando falta el token o es invalido/expirado en
 * un endpoint protegido -- por default Spring Security devuelve una
 * pagina de error generica (o el trace completo con devtools), esto lo
 * reemplaza por el mismo formato JSON que usa GlobalExceptionHandler.
 */
@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

    // No hay un bean ObjectMapper autoconfigurado en este proyecto (no
    // esta spring-boot-starter-json), asi que se arma uno propio.
    // findAndRegisterModules() suma el soporte de Instant/LocalDate, y
    // WRITE_DATES_AS_TIMESTAMPS en false lo serializa como fecha
    // ISO-8601, no como numero.
    private final ObjectMapper objectMapper = new ObjectMapper()
            .findAndRegisterModules()
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", Instant.now());
        body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        body.put("error", "Unauthorized");
        body.put("message", "Hace falta un token valido (header Authorization: Bearer <token>) para acceder a este recurso");
        body.put("path", request.getRequestURI());

        objectMapper.writeValue(response.getOutputStream(), body);
    }
}
