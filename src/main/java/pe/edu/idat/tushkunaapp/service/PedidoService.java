package pe.edu.idat.tushkunaapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.idat.tushkunaapp.dto.PedidoForm;
import pe.edu.idat.tushkunaapp.model.Mesa;
import pe.edu.idat.tushkunaapp.model.Pedido;
import pe.edu.idat.tushkunaapp.repository.MesaRepository;
import pe.edu.idat.tushkunaapp.repository.PedidoRepository;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final MesaRepository mesaRepository;

    public List<Pedido> getPedidosPendientes() {
        return pedidoRepository.findByEstado("pendiente");
    }

    public List<Pedido> getPedidosEnCocina() {
        return pedidoRepository.findByEstado("en_cocina");
    }

    public Pedido crearPedido(PedidoForm form, String mozo) {
        Pedido pedido = new Pedido();
        pedido.setMesaNumero(form.getMesaNumero());
        pedido.setMozo(mozo);
        pedido.setDescripcion(form.getDescripcion());
        pedido.setTotal(form.getTotal());
        pedido.setEstado("en_cocina");
        pedido.setFecha(LocalDateTime.now());
        Pedido guardado = pedidoRepository.save(pedido);

        // Actualizar estado de la mesa a ocupada
        mesaRepository.findAll().stream()
                .filter(m -> m.getNumero().equals(form.getMesaNumero()))
                .findFirst()
                .ifPresent(m -> {
                    m.setEstado("ocupada");
                    mesaRepository.save(m);
                });

        return guardado;
    }

    public void marcarListo(Integer idpedido) {
        pedidoRepository.findById(idpedido).ifPresent(p -> {
            p.setEstado("listo");
            pedidoRepository.save(p);

            // Liberar la mesa
            mesaRepository.findAll().stream()
                    .filter(m -> m.getNumero().equals(p.getMesaNumero()))
                    .findFirst()
                    .ifPresent(m -> {
                        m.setEstado("libre");
                        mesaRepository.save(m);
                    });
        });
    }
}