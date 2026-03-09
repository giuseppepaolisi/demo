package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Risposta restituita dopo un login o registrazione avvenuti con successo.
 *
 * --- COSA CONTIENE LA RISPOSTA DI AUTENTICAZIONE ---
 *
 * accessToken:
 *   Il JWT che il client deve includere in ogni richiesta successiva.
 *   Header HTTP: "Authorization: Bearer <accessToken>"
 *   Scade dopo un tempo breve (es. 15 minuti, 1 ora).
 *
 * tokenType:
 *   Sempre "Bearer" per i JWT. Informa il client su come usare il token.
 *   RFC 6750 definisce lo standard Bearer token.
 *
 * expiresAt:
 *   Quando scade il token. Il client può usarlo per sapere quando deve
 *   fare una nuova autenticazione (o usare il refresh token).
 *
 * Dati utente (id, email, firstName, lastName, role):
 *   Comodo per il client: dopo il login sa subito chi è loggato
 *   senza dover fare un'altra chiamata GET /api/users/me.
 *   In alternativa questi dati potrebbero stare dentro il JWT stesso
 *   come "claims" (payload del token).
 *
 * --- REFRESH TOKEN (nota avanzata) ---
 *
 * In sistemi più completi si aggiunge un "refreshToken":
 *   - Ha una durata più lunga (es. 7 giorni, 30 giorni)
 *   - Viene usato per ottenere un nuovo accessToken quando quello
 *     corrente scade, senza richiedere all'utente di fare login di nuovo
 *   - Viene salvato nel DB per poterlo invalidare (logout, revoca accesso)
 *
 * Per semplicità questo progetto usa solo l'accessToken.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {

    /** Il JWT da usare nelle richieste successive */
    private String accessToken;

    /** Tipo di token: sempre "Bearer" per JWT */
    @Builder.Default
    private String tokenType = "Bearer";

    /** Quando scade il token */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private LocalDateTime expiresAt;

    private Long userId;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
}