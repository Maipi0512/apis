package com.uade.ecom.exception;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Punto unico donde se convierte cualquier excepcion en una respuesta
 * JSON prolija -- sin el stack trace de Java, que Spring devuelve por
 * default (y que con devtools activo se ve en cada error). El status
 * HTTP de cada excepcion propia (404, 409, etc.) se toma de su propio
 * @ResponseStatus, asi que no hay que repetirlo aca.
 *
 * Esto NO cubre los errores que tira Spring Security antes de llegar a
 * un controller (token ausente/invalido, rol insuficiente): esos los
 * maneja JwtAuthEntryPoint / JwtAccessDeniedHandler, configurados en
 * SeguridadConfig.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleBadCredentials(BadCredentialsException ex, HttpServletRequest request) {
        return build(HttpStatus.UNAUTHORIZED, "Email o contraseña incorrectos", request);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, Object>> handleAuthentication(AuthenticationException ex, HttpServletRequest request) {
        return build(HttpStatus.UNAUTHORIZED, "No se pudo autenticar la solicitud", request);
    }

    /**
     * El body no se pudo leer: JSON mal armado o un valor que no existe
     * (ej. unidadMedida "LITRO"). Es un error del que manda el pedido.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleBodyInvalido(HttpMessageNotReadableException ex, HttpServletRequest request) {
        return build(HttpStatus.BAD_REQUEST, "El cuerpo del pedido no es valido: revisa el JSON y los valores enviados", request);
    }

    /**
     * Un parametro de la URL con el tipo equivocado (ej. /categorias/abc).
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleParametroInvalido(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        return build(HttpStatus.BAD_REQUEST, "El valor '" + ex.getValue() + "' no es valido para " + ex.getName(), request);
    }

    /**
     * La imagen supera el limite de spring.servlet.multipart.max-file-size.
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Map<String, Object>> handleArchivoMuyGrande(MaxUploadSizeExceededException ex, HttpServletRequest request) {
        return build(HttpStatus.PAYLOAD_TOO_LARGE, "La imagen es muy grande (maximo 5 MB)", request);
    }

    /**
     * Cubre todas las excepciones propias del dominio (ResourceNotFoundException,
     * StockInsuficienteException, AccesoDenegadoException, etc.): todas
     * tienen @ResponseStatus, asi que el status y el mensaje salen de ahi.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex, HttpServletRequest request) {
        ResponseStatus responseStatus = ex.getClass().getAnnotation(ResponseStatus.class);

        if (responseStatus == null) {
            // No es una excepcion nuestra: no sabemos que la causo, asi
            // que no le mostramos el detalle al cliente (podria ser
            // informacion interna), pero lo dejamos en el log del server.
            log.error("Error no controlado", ex);
            return build(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrio un error inesperado", request);
        }

        return build(responseStatus.value(), ex.getMessage(), request);
    }

    private ResponseEntity<Map<String, Object>> build(HttpStatus status, String message, HttpServletRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", Instant.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        body.put("path", request.getRequestURI());
        return ResponseEntity.status(status).body(body);
    }
}
