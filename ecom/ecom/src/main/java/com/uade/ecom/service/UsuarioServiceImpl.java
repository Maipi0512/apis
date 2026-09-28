package com.uade.ecom.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.uade.ecom.dto.UsuarioUpdateDTO;
import com.uade.ecom.exception.EntidadEnUsoException;
import com.uade.ecom.model.Usuario;
import com.uade.ecom.repository.UsuarioRepository;
import com.uade.ecom.util.SecurityUtils;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public Usuario getUsuarioActual() {
        return SecurityUtils.getUsuarioActual();
    }

    /**
     * Solo toca nombre/apellido/email/direccion del usuario autenticado
     * -- nunca el rol (UsuarioUpdateDTO ni siquiera tiene ese campo) ni
     * la de otro usuario (no recibe ningun id, siempre es "a mi mismo").
     */
    @Override
    public Usuario actualizarUsuarioActual(UsuarioUpdateDTO usuarioUpdateDTO) {
        Usuario usuario = SecurityUtils.getUsuarioActual();

        if (StringUtils.hasText(usuarioUpdateDTO.getEmail()) && !usuarioUpdateDTO.getEmail().equals(usuario.getEmail())) {
            usuarioRepository.findByEmail(usuarioUpdateDTO.getEmail())
                    .filter(otro -> !otro.getId().equals(usuario.getId()))
                    .ifPresent(otro -> {
                        throw new EntidadEnUsoException(
                                "Ya existe un usuario registrado con el email " + usuarioUpdateDTO.getEmail());
                    });
            usuario.setEmail(usuarioUpdateDTO.getEmail());
        }

        if (StringUtils.hasText(usuarioUpdateDTO.getNombre())) {
            usuario.setNombre(usuarioUpdateDTO.getNombre());
        }
        if (StringUtils.hasText(usuarioUpdateDTO.getApellido())) {
            usuario.setApellido(usuarioUpdateDTO.getApellido());
        }
        if (usuarioUpdateDTO.getDireccion() != null) {
            usuario.setDireccion(usuarioUpdateDTO.getDireccion());
        }

        return usuarioRepository.save(usuario);
    }
}
