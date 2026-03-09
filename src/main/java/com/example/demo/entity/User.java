package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * Entità che rappresenta un Utente nel sistema.
 *
 * --- IL CONTRATTO UserDetails ---
 *
 * Spring Security non sa nulla della tua entità "User".
 * Per insegnargli a usarla per l'autenticazione, devi implementare
 * l'interfaccia UserDetails, che è il "contratto" che Spring Security
 * si aspetta da qualsiasi oggetto che rappresenti un utente autenticato.
 *
 * UserDetails richiede di implementare questi metodi:
 *
 *   getUsername()       → identificatore univoco dell'utente (qui: email)
 *   getPassword()       → password HASHATA (Spring la confronta con quella inserita)
 *   getAuthorities()    → lista di ruoli/permessi (es. [ROLE_USER, ROLE_ADMIN])
 *   isAccountNonExpired()    → false = account scaduto (es. trial period finito)
 *   isAccountNonLocked()     → false = account bloccato (es. troppi tentativi falliti)
 *   isCredentialsNonExpired() → false = password scaduta (es. forza cambio ogni 90 gg)
 *   isEnabled()              → false = account disabilitato (alternativa al delete)
 *
 */
@Entity
@Table(
        name = "users",
        indexes = {
                // Indice sull'email perché viene cercata spesso (login, verifica unicità)
                @Index(name = "idx_user_email", columnList = "email", unique = true)
        }
)
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@EqualsAndHashCode(of = "id") // equals e hashCode basati su id
@ToString(exclude = {"password"}) // Esclude la password dal toString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Il nome è obbligatorio")
    @Size(min = 2, max = 100)
    private String firstName;

    @Column(nullable = false)
    @NotBlank(message = "Il cognome è obbligatorio")
    @Size(min = 2, max = 100)
    private String lastName;

    @Column(nullable = false, unique = true)
    @NotBlank
    @Email(message = "Formato email non valido")
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Role role = Role.ROLE_USER; // Valore di default

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // =========================================================================
    // IMPLEMENTAZIONE UserDetails
    // Spring Security chiama questi metodi per autenticare e autorizzare l'utente
    // =========================================================================

    /**
     * Restituisce i ruoli/permessi dell'utente.
     *
     * GrantedAuthority → interfaccia Spring Security per un permesso/ruolo.
     * SimpleGrantedAuthority("ROLE_ADMIN") → implementazione concreta con stringa.
     *
     * Restituiamo il nome dell'enum (es. "ROLE_ADMIN") come authority.
     * Spring Security può poi verificare: .hasRole("ADMIN") o .hasAuthority("ROLE_ADMIN")
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Un utente ha un solo ruolo in questo sistema.
        // In sistemi più complessi: Set<Role> e return roles.stream().map(...).collect(...)
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    /**
     * Spring Security chiama questo metodo per trovare l'utente nel DB.
     */
    @Override
    public String getUsername() {
        return email; // email come username
    }

    /**
     * La password hashata. Spring Security la confronta con quella inserita dall'utente
     * usando il PasswordEncoder configurato (BCryptPasswordEncoder).
     */
    @Override
    public String getPassword() {
        return password;
    }

    // I tre metodi seguenti controllano lo stato dell'account.

    @Override
    public boolean isAccountNonExpired() {
        return true; // L'account non scade mai
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // L'account non viene mai bloccato per i tentativi falliti
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // La password non scade
    }

    @Override
    public boolean isEnabled() {
        return true; // Account sempre abilitato
    }

}