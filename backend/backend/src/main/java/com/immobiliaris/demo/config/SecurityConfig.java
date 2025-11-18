package com.immobiliaris.demo.config;

import com.immobiliaris.demo.service.CustomUserDetailsService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.cors.CorsConfigurationSource;

import java.io.IOException;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final CorsConfigurationSource corsConfigurationSource;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService, 
                         CorsConfigurationSource corsConfigurationSource) {
        this.customUserDetailsService = customUserDetailsService;
        this.corsConfigurationSource = corsConfigurationSource;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        AuthenticationSuccessHandler successHandler = new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                boolean isAdmin = authentication.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
                boolean isAgent = authentication.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_AGENT"));

                if (isAdmin) {
                    response.sendRedirect("/admin");
                } else if (isAgent) {
                    response.sendRedirect("/agent");
                } else {
                    response.sendRedirect("/");
                }
            }
        };

        http
            .cors(cors -> cors.configurationSource(this.corsConfigurationSource))
<<<<<<< HEAD
            .csrf(csrf -> csrf.disable())  // Disabilita CSRF per API REST (usa JWT in produzione)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/api/auth/**", "/api/mail/**", "/login", "/error", "/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()
=======
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/api/auth/**", "/api/mail/**", "/login", "/error", "/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()
                .requestMatchers("/api/users/register").hasRole("ADMIN")
>>>>>>> main
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/agent/**").hasRole("AGENT")
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/agent/**").hasRole("AGENT")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/error")  // Reindirizza a /error invece di /login
                .usernameParameter("email")
                .passwordParameter("password")
                .successHandler(successHandler)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/error")  // Dopo logout vai a /error
                .permitAll()
            )
            .exceptionHandling(ex -> ex
<<<<<<< HEAD
                .accessDeniedPage("/error")  // Reindirizza a /error per 403 Forbidden
=======
                .accessDeniedPage("/error")
>>>>>>> main
                .authenticationEntryPoint((request, response, authException) -> {
                    // Non reindirizzare le API REST, ritorna 401
                    String requestUri = request.getRequestURI();
                    if (requestUri.startsWith("/api/")) {
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                    } else {
                        // Per le pagine HTML, vai a /error
                        response.sendRedirect("/error");
                    }
                })
            );

        return http.build();
    }

    @Bean
    @SuppressWarnings("deprecation")
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(passwordEncoder());
        authProvider.setUserDetailsService(customUserDetailsService);
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * AuthenticationManager per API REST login
     * Necessario per permettere login tramite JSON
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
