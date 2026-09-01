package com.uade.ecom.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.ecom.dto.PagoRequestDTO;
import com.uade.ecom.exception.DatoInvalidoException;
import com.uade.ecom.exception.PagoInvalidoException;
import com.uade.ecom.exception.ResourceNotFoundException;
import com.uade.ecom.model.Pago;
import com.uade.ecom.model.Pedido;
import com.uade.ecom.repository.PagoRepository;
import com.uade.ecom.repository.PedidoRepository;

@Service
public class PagoServiceImpl implements PagoService {

    private static final String ESTADO_CANCELADO = "CANCELADO";
    private static final String ESTADO_PAGADO = "PAGADO";
    private static final String ESTADO_PENDIENTE = "PENDIENTE";

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Override
    public List<Pago> getAllPagos() {
        return pagoRepository.findAll();
    }

    @Override
    public Pago getPagoById(Long id) {
        return pagoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro ningun pago con id " + id));
    }

    @Override
    public Pago createPago(PagoRequestDTO pagoRequestDTO) {
        Pedido pedido = pedidoRepository.findById(pagoRequestDTO.getPedidoId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontro ningun pedido con id " + pagoRequestDTO.getPedidoId()));

        validarPagoPermitido(pedido, pagoRequestDTO.getMonto(), null);

        Pago pago = new Pago();
        pago.setPedido(pedido);
        pago.setMetodoPago(pagoRequestDTO.getMetodoPago());
        pago.setMonto(pagoRequestDTO.getMonto());
        Pago pagoGuardado = pagoRepository.save(pago);

        actualizarEstadoSegunPagos(pedido);

        return pagoGuardado;
    }

    @Override
    public Pago updatePago(Long id, PagoRequestDTO pagoRequestDTO) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro ningun pago con id " + id));

        Pedido pedidoNuevo = pedidoRepository.findById(pagoRequestDTO.getPedidoId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontro ningun pedido con id " + pagoRequestDTO.getPedidoId()));

        validarPagoPermitido(pedidoNuevo, pagoRequestDTO.getMonto(), id);

        Pedido pedidoAnterior = pago.getPedido();

        pago.setPedido(pedidoNuevo);
        pago.setMetodoPago(pagoRequestDTO.getMetodoPago());
        pago.setMonto(pagoRequestDTO.getMonto());
        Pago pagoActualizado = pagoRepository.save(pago);

        actualizarEstadoSegunPagos(pedidoNuevo);
        if (!pedidoAnterior.getId().equals(pedidoNuevo.getId())) {
            revertirEstadoSiCorresponde(pedidoAnterior);
        }

        return pagoActualizado;
    }

    @Override
    public void deletePago(Long id) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro ningun pago con id " + id));

        Pedido pedido = pago.getPedido();
        pagoRepository.delete(pago);

        revertirEstadoSiCorresponde(pedido);
    }

    /**
     * Un pago solo se puede registrar si el pedido no esta CANCELADO, y
     * si el monto no supera el saldo que todavia queda pendiente.
     * pagoIdAExcluir se usa en el update: no queremos contar el monto
     * viejo del pago que se esta editando como si ya estuviera pagado.
     */
    private void validarPagoPermitido(Pedido pedido, BigDecimal monto, Long pagoIdAExcluir) {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DatoInvalidoException("El monto del pago tiene que ser mayor a 0");
        }

        if (ESTADO_CANCELADO.equals(pedido.getEstado())) {
            throw new PagoInvalidoException(
                    "No se puede registrar un pago para el pedido " + pedido.getId() + " porque esta CANCELADO");
        }

        BigDecimal totalPagado = totalPagado(pedido.getId(), pagoIdAExcluir);
        BigDecimal saldoPendiente = pedido.getTotal().subtract(totalPagado);

        if (monto.compareTo(saldoPendiente) > 0) {
            throw new PagoInvalidoException(
                    "El pago de " + monto + " supera el saldo pendiente del pedido " + pedido.getId()
                            + " (saldo: " + saldoPendiente + ")");
        }
    }

    /**
     * Cuando la suma de pagos llega a cubrir el total, el pedido pasa
     * solo a PAGADO (nadie tiene que tocar el estado a mano).
     */
    private void actualizarEstadoSegunPagos(Pedido pedido) {
        BigDecimal totalPagado = totalPagado(pedido.getId(), null);

        if (totalPagado.compareTo(pedido.getTotal()) >= 0 && !ESTADO_PAGADO.equals(pedido.getEstado())) {
            pedido.setEstado(ESTADO_PAGADO);
            pedidoRepository.save(pedido);
        }
    }

    /**
     * Si se borra o se mueve un pago y eso hace que un pedido PAGADO ya
     * no tenga cubierto el total, vuelve a PENDIENTE.
     */
    private void revertirEstadoSiCorresponde(Pedido pedido) {
        if (!ESTADO_PAGADO.equals(pedido.getEstado())) {
            return;
        }

        BigDecimal totalPagado = totalPagado(pedido.getId(), null);
        if (totalPagado.compareTo(pedido.getTotal()) < 0) {
            pedido.setEstado(ESTADO_PENDIENTE);
            pedidoRepository.save(pedido);
        }
    }

    private BigDecimal totalPagado(Long pedidoId, Long pagoIdAExcluir) {
        return pagoRepository.findByPedidoId(pedidoId).stream()
                .filter(p -> pagoIdAExcluir == null || !p.getId().equals(pagoIdAExcluir))
                .map(Pago::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
