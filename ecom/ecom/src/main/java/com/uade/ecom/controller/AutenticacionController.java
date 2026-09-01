package com.uade.ecom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.ecom.dto.auth.AutenticacionResponseDTO;
import com.uade.ecom.dto.auth.LoginRequestDTO;
import com.uade.ecom.dto.auth.RegistroRequestDTO;
import com.uade.ecom.service.AutenticacionService;

@RestController
@RequestMapping("/auth")
public class AutenticacionController {

    @Autowired
    private AutenticacionService autenticacionService;

    @PostMapping("/registro")
    public AutenticacionResponseDTO registrar(@RequestBody RegistroRequestDTO registroRequestDTO) {
        return autenticacionService.registrar(registroRequestDTO);
    }

    @PostMapping("/login")
    public AutenticacionResponseDTO login(@RequestBody LoginRequestDTO loginRequestDTO) {
        return autenticacionService.login(loginRequestDTO);
    }
}
