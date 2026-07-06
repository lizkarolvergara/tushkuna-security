package pe.edu.idat.tushkunaapp.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(1)
public class XssFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String path = httpRequest.getRequestURI();

        // No aplicar el filtro a rutas de auth
        if (path.startsWith("/auth/") || path.equals("/") || path.equals("/logout")) {
            chain.doFilter(request, response);
            return;
        }

        chain.doFilter(new XssRequestWrapper(httpRequest), response);
    }
}