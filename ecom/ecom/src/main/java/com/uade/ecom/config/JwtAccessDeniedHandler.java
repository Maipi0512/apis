package com.uade.ecom.config;

import java.io.IOException;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Que responde la API cuando el usuario esta autenticado pero su rol no
 * alcanza (ej. un CLIENTE intentando POST /categorias, que es solo de
 * ADMIN) -- mismo formato JSON que el resto de los errores, sin trace.
 */
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    // No hay un bean ObjectMapper autoconfigurado en este proyecto (no
    // esta spring-boot-starter-json), asi que se arma uno propio.
    // findAndRegisterModules() suma el soporte de Instant/LocalDate, y
    // WRITE_DATES_AS_TIMESTAMPS en false lo serializa como fecha
    // ISO-8601, no como numero.
    private final ObjectMapper objectMapper = new ObjectMapper()
            .findAndRegisterModules()
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", Instant.now());
        body.put("status", HttpServletResponse.SC_FORBIDDEN);
        body.put("error", "Forbidden");
        body.put("message", "No tenes permiso para acceder a este recurso");
        body.put("path", request.getRequestURI());

        objectMapper.writeValue(response.getOutputStream(), body);
    }
}
