package com.uade.ecom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.ecom.dto.UsuarioResponseDTO;
import com.uade.ecom.dto.UsuarioUpdateDTO;
import com.uade.ecom.service.UsuarioService;

/**
 * Perfil del usuario autenticado. No hay "/usuarios/{id}": nadie edita
 * ni consulta los datos de otro, solo los propios (ver
 * SecurityUtils.getUsuarioActual() en UsuarioServiceImpl) -- asi no
 * hace falta validar "es mio o soy admin" como en Carrito/Pedido.
 */
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/me")
    public UsuarioResponseDTO getUsuarioActual() {
        return UsuarioResponseDTO.from(usuarioService.getUsuarioActual());
    }

    /**
     * Edita nombre/apellido/email/direccion del usuario autenticado.
     * El rol NUNCA se puede tocar aca -- UsuarioUpdateDTO no tiene ese
     * campo, ni siquiera llega a leerse si lo mandan en el body.
     */
    @PutMapping("/me")
    public UsuarioResponseDTO actualizarUsuarioActual(@RequestBody UsuarioUpdateDTO usuarioUpdateDTO) {
        return UsuarioResponseDTO.from(usuarioService.actualizarUsuarioActual(usuarioUpdateDTO));
    }
}
