package com.uade.ecom.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.uade.ecom.dto.PagoRequestDTO;
import com.uade.ecom.model.Pago;
import com.uade.ecom.service.PagoService;

@RestController
@RequestMapping("/pagos")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @GetMapping
    public List<Pago> getAllPagos() {
        return pagoService.getAllPagos();
    }

    @GetMapping("/{id}")
    public Pago getPagoById(@PathVariable Long id) {
        return pagoService.getPagoById(id);
    }

    @PostMapping
    public Pago createPago(@RequestBody PagoRequestDTO pagoRequestDTO) {
        return pagoService.createPago(pagoRequestDTO);
    }

    @PutMapping("/{id}")
    public Pago updatePago(@PathVariable Long id, @RequestBody PagoRequestDTO pagoRequestDTO) {
        return pagoService.updatePago(id, pagoRequestDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePago(@PathVariable Long id) {
        pagoService.deletePago(id);
    }
}
