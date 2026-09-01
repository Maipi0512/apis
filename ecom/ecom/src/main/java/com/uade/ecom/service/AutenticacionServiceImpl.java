package com.uade.ecom.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.uade.ecom.config.JwtService;
import com.uade.ecom.dto.auth.AutenticacionResponseDTO;
import com.uade.ecom.dto.auth.LoginRequestDTO;
import com.uade.ecom.dto.auth.RegistroRequestDTO;
import com.uade.ecom.exception.EntidadEnUsoException;
import com.uade.ecom.model.Usuario;
import com.uade.ecom.repository.UsuarioRepository;

@Service
public class AutenticacionServiceImpl implements AutenticacionService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public AutenticacionResponseDTO registrar(RegistroRequestDTO registroRequestDTO) {
        if (usuarioRepository.existsByEmail(registroRequestDTO.getEmail())) {
            throw new EntidadEnUsoException(
                    "Ya existe un usuario registrado con el email " + registroRequestDTO.getEmail());
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(registroRequestDTO.getNombre());
        usuario.setApellido(registroRequestDTO.getApellido());
        usuario.setEmail(registroRequestDTO.getEmail());
        usuario.setPassword(passwordEncoder.encode(registroRequestDTO.getPassword()));
        usuario.setRol(registroRequestDTO.getRol());

        usuarioRepository.save(usuario);

        String token = jwtService.generateToken(usuario);
        return new AutenticacionResponseDTO(token);
    }

    @Override
    public AutenticacionResponseDTO login(LoginRequestDTO loginRequestDTO) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.getEmail(),
                        loginRequestDTO.getPassword()));

        Usuario usuario = usuarioRepository.findByEmail(loginRequestDTO.getEmail())
                .orElseThrow();

        String token = jwtService.generateToken(usuario);
        return new AutenticacionResponseDTO(token);
    }
}
