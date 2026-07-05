package pe.edu.idat.tushkunaapp.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import pe.edu.idat.tushkunaapp.service.DetalleUsuarioService;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final DetalleUsuarioService detalleUsuarioService;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain config(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/auth/login",
                                "/auth/registrar",
                                "/auth/guardarUsuario",
                                "/resources/**",
                                "/static/**",
                                "/styles/**",
                                "/scripts/**")
                        .permitAll()
                        .requestMatchers("/tushkuna/admin/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/tushkuna/mozo/**").hasAuthority("ROLE_MOZO")
                        .requestMatchers("/tushkuna/cocina/**").hasAuthority("ROLE_COCINA")
                        .anyRequest().authenticated()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/auth/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .authenticationProvider(authenticationProvider());

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider dao =
                new DaoAuthenticationProvider(detalleUsuarioService);
        dao.setPasswordEncoder(passwordEncoder);
        return dao;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}