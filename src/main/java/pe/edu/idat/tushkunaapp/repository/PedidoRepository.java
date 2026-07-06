package pe.edu.idat.tushkunaapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.idat.tushkunaapp.model.Pedido;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
    List<Pedido> findByEstado(String estado);
    List<Pedido> findByMesaNumero(Integer mesaNumero);
}