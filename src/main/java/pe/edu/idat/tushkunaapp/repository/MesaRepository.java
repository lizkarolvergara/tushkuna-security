package pe.edu.idat.tushkunaapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.idat.tushkunaapp.model.Mesa;
import java.util.List;

public interface MesaRepository extends JpaRepository<Mesa, Integer> {
    List<Mesa> findAllByOrderByNumeroAsc();
}