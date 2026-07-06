package pe.edu.idat.tushkunaapp.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pe.edu.idat.tushkunaapp.dto.LoginForm;
import pe.edu.idat.tushkunaapp.dto.UsuarioSeguridadDto;
import pe.edu.idat.tushkunaapp.model.Usuario;
import pe.edu.idat.tushkunaapp.service.RecaptchaService;
import pe.edu.idat.tushkunaapp.service.UsuarioService;

@RequiredArgsConstructor
@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UsuarioService usuarioService;
    private final AuthenticationManager authManager;
    private final RecaptchaService captchaService;

    @Value("${recaptcha.site-key}")
    private String recaptchaSiteKey;


    // GET /auth/login
    @GetMapping("/login")
    public String login(Model model,
                        @RequestParam(required = false) String logout,
                        @RequestParam(required = false) String error) {
        model.addAttribute("form", new LoginForm());
        model.addAttribute("recaptchaSiteKey", recaptchaSiteKey);
        if (logout != null)
            model.addAttribute("infoMsg", "Sesión cerrada correctamente");
        if (error != null)
            model.addAttribute("errorMsg", "Usuario o contraseña incorrectos");
        return "auth/frmLogin";
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/auth/login";
    }

    // POST /auth/login
    @PostMapping("/login")
    public String doLogin(@Valid @ModelAttribute("form") LoginForm form,
                          BindingResult br,
                          @RequestParam(name = "g-recaptcha-response", required = false)
                          String recaptchaToken,
                          HttpServletRequest request,
                          Model model) {

        model.addAttribute("recaptchaSiteKey", recaptchaSiteKey);

        if (br.hasErrors()) {
            return "auth/frmLogin";
        }

        boolean captchaOk = captchaService.verify(recaptchaToken, request.getRemoteAddr());
        if (!captchaOk) {
            model.addAttribute("errorMsg", "Por favor completa el CAPTCHA correctamente");
            return "auth/frmLogin";
        }

        try {
            var authRequest = new UsernamePasswordAuthenticationToken(
                    form.getUsername(), form.getPassword());
            var authResult = authManager.authenticate(authRequest);

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authResult);
            SecurityContextHolder.setContext(context);

            HttpSession session = request.getSession(true);
            session.setAttribute(
                    HttpSessionSecurityContextRepository
                            .SPRING_SECURITY_CONTEXT_KEY, context);

            // Redireccion por rol
            String rol = authResult.getAuthorities().iterator().next().getAuthority();

            return switch (rol) {
                case "ROLE_ADMIN" -> "redirect:/tushkuna/admin";
                case "ROLE_MOZO" -> "redirect:/tushkuna/mozo";
                case "ROLE_COCINA" -> "redirect:/tushkuna/cocina";
                default -> "redirect:/auth/home";
            };

        } catch (Exception e) {
            model.addAttribute("errorMsg", "Usuario o contraseña incorrectos");
            return "auth/frmLogin";
        }
    }

    // GET /auth/registrar
    @GetMapping("/registrar")
    public String registrar(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "auth/frmRegistroUsuario";
    }

    // POST /auth/guardarUsuario
    @PostMapping("/guardarUsuario")
    public String guardarUsuario(@ModelAttribute Usuario usuario) {
        usuarioService.saveUser(usuario, 1); // rol 1 = ROLE_ADMIN por defecto
        return "redirect:/auth/login";
    }

    // GET /auth/login-success
    @GetMapping("/login-success")
    public String loginSuccess() {
        return "redirect:/auth/home";
    }

    // GET /auth/home
    @GetMapping("/home")
    public String home(@AuthenticationPrincipal UsuarioSeguridadDto dto,
                       Model model) {
        if (dto == null) return "redirect:/auth/login";
        model.addAttribute("nombre", dto.getUsername());
        model.addAttribute("email", dto.getEmail());
        model.addAttribute("rol", dto.getRol());
        return "auth/home";
    }
}