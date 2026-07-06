package pe.edu.idat.tushkunaapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.idat.tushkunaapp.model.Mesa;
import pe.edu.idat.tushkunaapp.repository.MesaRepository;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MesaService {

    private final MesaRepository mesaRepository;

    public List<Mesa> getAllMesas() {
        return mesaRepository.findAllByOrderByNumeroAsc();
    }

    public Mesa getMesaById(Integer id) {
        return mesaRepository.findById(id).orElse(null);
    }

    public void actualizarEstado(Integer idmesa, String estado) {
        Mesa mesa = mesaRepository.findById(idmesa).orElse(null);
        if (mesa != null) {
            mesa.setEstado(estado);
            mesaRepository.save(mesa);
        }
    }
}