package com.uade.ecom.config;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Cadena de filtros de Spring Security: que endpoints son publicos y
 * cuales requieren JWT, mas el orden en que se aplica el filtro propio
 * (JwtAutenticacionFilter) respecto del filtro de Spring.
 *
 * "/auth/**" queda publico (ahi es donde se registra/loguea un usuario
 * para conseguir el token). El catalogo (categorias, productos,
 * proveedores) se puede LEER con cualquier usuario logueado, pero
 * crearlo/editarlo/borrarlo es solo de ADMIN. Cambiar el estado de un
 * Pedido (PUT) tambien es solo de ADMIN. El resto (carritos, items de
 * carrito, pedidos, pagos, detalles de pedido) queda para cualquier
 * usuario autenticado -- el filtro de "es mio o soy admin" para Carrito
 * y Pedido se hace en el service (ver CarritoServiceImpl/PedidoServiceImpl),
 * porque Spring Security por si solo no sabe de quien es cada fila.
 */
@Configuration
@EnableWebSecurity
public class SeguridadConfig {

    @Autowired
    private JwtAutenticacionFilter jwtAuthFilter;

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req -> req
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/error/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/categorias/**", "/productos/**", "/proveedores/**")
                        .authenticated()
                        .requestMatchers(HttpMethod.POST, "/categorias/**", "/productos/**", "/proveedores/**")
                        .hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/categorias/**", "/productos/**", "/proveedores/**")
                        .hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/categorias/**", "/productos/**", "/proveedores/**")
                        .hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/pedidos/**").hasAuthority("ADMIN")
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
