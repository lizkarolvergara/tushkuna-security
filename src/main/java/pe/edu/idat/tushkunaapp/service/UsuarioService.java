package pe.edu.idat.tushkunaapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pe.edu.idat.tushkunaapp.model.Rol;
import pe.edu.idat.tushkunaapp.model.Usuario;
import pe.edu.idat.tushkunaapp.repository.UsuarioRepository;

@RequiredArgsConstructor
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public Usuario getUsuarioByNomusuario(String nomusuario) {
        return usuarioRepository.findByNomusuario(nomusuario);
    }

    public Usuario saveUser(Usuario usuario, Integer idrol) {
        // Cifrado con Argon2
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        Rol rol = new Rol();
        rol.setIdrol(idrol);
        usuario.setRol(rol);
        usuario.setActivo(true);
        return usuarioRepository.save(usuario);
    }
}