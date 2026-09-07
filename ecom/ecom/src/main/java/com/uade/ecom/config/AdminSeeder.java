package com.uade.ecom.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.uade.ecom.model.Rol;
import com.uade.ecom.model.Usuario;
import com.uade.ecom.repository.UsuarioRepository;

/**
 * Crea el usuario ADMIN al arrancar la app, si todavia no existe
 * ninguno. Es el UNICO lugar donde se crea un ADMIN: el registro
 * publico (POST /auth/registro) siempre crea CLIENTE (ver
 * RegistroRequestDTO / AutenticacionServiceImpl), para que nadie pueda
 * auto-asignarse el rol de administrador.
 *
 * Email y password salen de application.properties (ADMIN_EMAIL /
 * ADMIN_PASSWORD en application-local.properties), con un valor por
 * defecto para que la app arranque sin configurar nada extra.
 */
@Component
public class AdminSeeder implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${application.admin.email}")
    private String adminEmail;

    @Value("${application.admin.password}")
    private String adminPassword;

    @Override
    public void run(String... args) {
        if (usuarioRepository.existsByRol(Rol.ADMIN)) {
            return;
        }

        Usuario admin = new Usuario();
        admin.setNombre("Admin");
        admin.setApellido("Ecom");
        admin.setEmail(adminEmail);
        admin.setPassword(passwordEncoder.encode(adminPassword));
        admin.setRol(Rol.ADMIN);
        usuarioRepository.save(admin);
    }
}
