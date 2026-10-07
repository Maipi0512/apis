package com.uade.ecom.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uade.ecom.dto.PagoRequestDTO;
import com.uade.ecom.exception.AccesoDenegadoException;
import com.uade.ecom.exception.DatoInvalidoException;
import com.uade.ecom.exception.ResourceNotFoundException;
import com.uade.ecom.model.MetodoPago;
import com.uade.ecom.model.Pago;
import com.uade.ecom.model.Pedido;
import com.uade.ecom.model.Usuario;
import com.uade.ecom.repository.PagoRepository;
import com.uade.ecom.util.SecurityUtils;

/**
 * Flujo tipo Mercado Pago: el cliente arma el carrito, elige el metodo de
 * pago y paga. Recien ahi se crea el Pedido. El monto no lo manda el
 * cliente, sale del total del carrito. Un pago registrado no se edita ni
 * se borra (si hay que devolver la plata, se cancela el pedido).
 */
@Service
public class PagoServiceImpl implements PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private CarritoService carritoService;

    @Override
    public List<Pago> getAllPagos() {
        if (SecurityUtils.esAdmin()) {
            return pagoRepository.findAll();
        }
        return pagoRepository.findByPedido_Usuario_Id(SecurityUtils.getUsuarioActual().getId());
    }

    @Override
    public Pago getPagoById(Long id) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro ningun pago con id " + id));
        validarDueño(pago);
        return pago;
    }

    @Override
    @Transactional
    public Pago pagar(PagoRequestDTO pagoRequestDTO) {
        String recibido = pagoRequestDTO.getMetodoPago();
        if (recibido == null || recibido.isBlank()) {
            throw new DatoInvalidoException(
                    "Hay que elegir un metodo de pago: mandar { \"metodoPago\": \"...\" } con uno de "
                            + Arrays.toString(MetodoPago.values()));
        }
        MetodoPago metodoPago = MetodoPago.parse(recibido)
                .orElseThrow(() -> new DatoInvalidoException(
                        "Metodo de pago invalido: '" + recibido + "'. Opciones: "
                                + Arrays.toString(MetodoPago.values())));

        // La direccion es opcional al registrarse, pero para comprar hace
        // falta: sin direccion no hay a donde mandar el pedido.
        Usuario usuario = SecurityUtils.getUsuarioActual();
        if (usuario.getDireccion() == null || usuario.getDireccion().isBlank()) {
            throw new DatoInvalidoException(
                    "Falta la direccion de envio: cargala en Mi cuenta antes de pagar");
        }

        // Todo en la misma transaccion: si falla el pago, no queda un
        // pedido creado ni stock descontado.
        Pedido pedido = carritoService.crearPedidoPagado();

        Pago pago = new Pago();
        pago.setPedido(pedido);
        pago.setMetodoPago(metodoPago.name());
        pago.setMonto(pedido.getTotal());
        return pagoRepository.save(pago);
    }

    /**
     * Un CLIENTE solo puede ver sus propios pagos; un ADMIN puede ver
     * cualquiera.
     */
    private void validarDueño(Pago pago) {
        if (SecurityUtils.esAdmin()) {
            return;
        }
        Usuario actual = SecurityUtils.getUsuarioActual();
        Usuario dueño = pago.getPedido().getUsuario();
        if (dueño == null || !dueño.getId().equals(actual.getId())) {
            throw new AccesoDenegadoException("El pago " + pago.getId() + " no pertenece al usuario autenticado");
        }
    }
}
