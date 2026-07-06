package pe.edu.idat.tushkunaapp.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pe.edu.idat.tushkunaapp.dto.PedidoForm;
import pe.edu.idat.tushkunaapp.dto.UsuarioSeguridadDto;
import pe.edu.idat.tushkunaapp.service.MesaService;
import pe.edu.idat.tushkunaapp.service.PedidoService;

@Controller
@RequestMapping("/tushkuna")
@RequiredArgsConstructor
public class TushkunaController {

    private final MesaService mesaService;
    private final PedidoService pedidoService;

    // ADMIN
    @GetMapping("/admin")
    public String admin(@AuthenticationPrincipal UsuarioSeguridadDto dto,
                        Model model) {
        model.addAttribute("nombre", dto.getNombres());
        model.addAttribute("usuario", dto.getUsername());
        model.addAttribute("pedidosPendientes",
                pedidoService.getPedidosPendientes().size());
        model.addAttribute("pedidosEnCocina",
                pedidoService.getPedidosEnCocina().size());
        model.addAttribute("totalMesas",
                mesaService.getAllMesas().size());
        return "tushkuna/admin";
    }

    // MOZO - ver mesas
    @GetMapping("/mozo")
    public String mozo(@AuthenticationPrincipal UsuarioSeguridadDto dto,
                       Model model) {
        model.addAttribute("nombre", dto.getNombres());
        model.addAttribute("usuario", dto.getUsername());
        model.addAttribute("mesas", mesaService.getAllMesas());
        model.addAttribute("pedidoForm", new PedidoForm());
        return "tushkuna/mozo";
    }

    // MOZO - crear pedido
    @PostMapping("/mozo/pedido")
    public String crearPedido(@Valid @ModelAttribute("pedidoForm") PedidoForm form,
                              BindingResult br,
                              @AuthenticationPrincipal UsuarioSeguridadDto dto,
                              Model model) {
        if (br.hasErrors()) {
            model.addAttribute("nombre", dto.getNombres());
            model.addAttribute("usuario", dto.getUsername());
            model.addAttribute("mesas", mesaService.getAllMesas());
            return "tushkuna/mozo";
        }
        pedidoService.crearPedido(form, dto.getUsername());
        return "redirect:/tushkuna/mozo";
    }

    // COCINA - ver pedidos
    @GetMapping("/cocina")
    public String cocina(@AuthenticationPrincipal UsuarioSeguridadDto dto,
                         Model model) {
        model.addAttribute("nombre", dto.getNombres());
        model.addAttribute("usuario", dto.getUsername());
        model.addAttribute("pedidosEnCocina",
                pedidoService.getPedidosEnCocina());
        model.addAttribute("pedidosListos",
                pedidoService.getPedidosPendientes());
        return "tushkuna/cocina";
    }

    // COCINA - marcar listo
    @PostMapping("/cocina/listo/{id}")
    public String marcarListo(@PathVariable Integer id) {
        pedidoService.marcarListo(id);
        return "redirect:/tushkuna/cocina";
    }
}