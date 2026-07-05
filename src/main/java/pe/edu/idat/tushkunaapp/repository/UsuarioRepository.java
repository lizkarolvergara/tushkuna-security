package pe.edu.idat.tushkunaapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.idat.tushkunaapp.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Usuario findByNomusuario(String nomusuario);
}