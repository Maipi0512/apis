package com.uade.ecom.service;

import com.uade.ecom.dto.UsuarioUpdateDTO;
import com.uade.ecom.model.Usuario;

public interface UsuarioService {

    Usuario getUsuarioActual();

    Usuario actualizarUsuarioActual(UsuarioUpdateDTO usuarioUpdateDTO);
}
