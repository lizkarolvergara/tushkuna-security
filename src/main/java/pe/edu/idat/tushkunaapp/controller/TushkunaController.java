package pe.edu.idat.tushkunaapp.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pe.edu.idat.tushkunaapp.dto.UsuarioSeguridadDto;

@Controller
@RequestMapping("/tushkuna")
public class TushkunaController {

    @GetMapping("/admin")
    public String admin(@AuthenticationPrincipal UsuarioSeguridadDto dto,
                        Model model) {
        model.addAttribute("nombre", dto.getNombres());
        model.addAttribute("usuario", dto.getUsername());
        return "tushkuna/admin";
    }

    @GetMapping("/mozo")
    public String mozo(@AuthenticationPrincipal UsuarioSeguridadDto dto,
                       Model model) {
        model.addAttribute("nombre", dto.getNombres());
        model.addAttribute("usuario", dto.getUsername());
        return "tushkuna/mozo";
    }

    @GetMapping("/cocina")
    public String cocina(@AuthenticationPrincipal UsuarioSeguridadDto dto,
                         Model model) {
        model.addAttribute("nombre", dto.getNombres());
        model.addAttribute("usuario", dto.getUsername());
        return "tushkuna/cocina";
    }
}