package com.example.demo.config;
// security/JwtAuthenticationFilter.java

import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

// Eseguito su OGNI richiesta HTTP prima che arrivi al controller
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Leggi l'header Authorization
        final String authHeader = request.getHeader("Authorization");

        // 2. Se non c'è o non inizia con "Bearer " → lascia passare
        //    (sarà Spring Security a bloccare se la route è protetta)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Estrai il token (rimuovi "Bearer ")
        final String jwt = authHeader.substring(7);

        try {
            // 4. Estrai l'email dal token
            final String email = jwtService.extractEmail(jwt);

            // 5. Se l'email è valida e l'utente non è già autenticato
            if (email != null && SecurityContextHolder.getContext()
                    .getAuthentication() == null) {

                // 6. Carica l'utente dal DB
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                // 7. Valida il token
                if (jwtService.isTokenValid(jwt, userDetails)) {

                    // 8. Crea il token di autenticazione per Spring Security
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,                           // credenziali (non servono dopo login)
                                    userDetails.getAuthorities()    // ruoli
                            );

                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    // 9. Salva nel SecurityContext → utente autenticato per questa richiesta
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            // Token malformato, scaduto, firma non valida → non autenticare
            // Lascia che Spring Security gestisca il 401
        }

        // 10. Continua la catena dei filtri → arriva al controller
        filterChain.doFilter(request, response);
    }
}