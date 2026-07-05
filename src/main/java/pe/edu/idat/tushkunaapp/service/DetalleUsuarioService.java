package pe.edu.idat.tushkunaapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pe.edu.idat.tushkunaapp.dto.UsuarioSeguridadDto;
import pe.edu.idat.tushkunaapp.model.Rol;
import pe.edu.idat.tushkunaapp.model.Usuario;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
public class DetalleUsuarioService implements UserDetailsService {

    private final UsuarioService usuarioService;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        Usuario usuario = usuarioService.getUsuarioByNomusuario(username);
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + username);
        }
        return getUsuarioSeguridadDto(usuario,
                grantedAuthorityList(usuario.getRol()));
    }

    private List<GrantedAuthority> grantedAuthorityList(Rol rol) {
        if (rol == null)
            return Collections.emptyList();
        return List.of(new SimpleGrantedAuthority(rol.getNomrol()));
    }

    private UsuarioSeguridadDto getUsuarioSeguridadDto(
            Usuario usuario, List<GrantedAuthority> grantedAuthorities) {
        UsuarioSeguridadDto dto = new UsuarioSeguridadDto(
                usuario.getNomusuario(),
                usuario.getPassword(),
                usuario.getActivo(),
                true,
                true,
                true,
                grantedAuthorities);
        dto.setEmail(usuario.getEmail());
        dto.setNombres(usuario.getNombres());
        dto.setRol(usuario.getRol() != null ? usuario.getRol().getNomrol() : "");
        return dto;
    }
}