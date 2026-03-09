package com.example.demo.entity;

/**
 * Enum che rappresenta i ruoli disponibili nel sistema.
 * --- CONVENZIONE SPRING SECURITY ---
 *
 * Spring Security distingue tra:
 *
 * ROLE (ruolo):
 *   - Prefisso "ROLE_" obbligatorio internamente
 *   - Usato con .hasRole("USER") → Spring aggiunge automaticamente il prefisso "ROLE_"
 *   - Esempio: .hasRole("ADMIN") verifica "ROLE_ADMIN" nell'authority
 *
 * AUTHORITY (permesso granulare):
 *   - Nessun prefisso obbligatorio
 *   - Usato con .hasAuthority("PRODUCT_WRITE")
 *   - Più granulare dei ruoli
 *
 * Il nome dell'enum includerà il prefisso "ROLE_" per compatibilità diretta con Spring Security.
 */
public enum Role {

    ROLE_USER,
    ROLE_ADMIN
}