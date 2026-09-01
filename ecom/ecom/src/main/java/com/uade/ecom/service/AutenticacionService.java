package com.uade.ecom.service;

import com.uade.ecom.dto.auth.AutenticacionResponseDTO;
import com.uade.ecom.dto.auth.LoginRequestDTO;
import com.uade.ecom.dto.auth.RegistroRequestDTO;

public interface AutenticacionService {

    AutenticacionResponseDTO registrar(RegistroRequestDTO registroRequestDTO);

    AutenticacionResponseDTO login(LoginRequestDTO loginRequestDTO);
}
