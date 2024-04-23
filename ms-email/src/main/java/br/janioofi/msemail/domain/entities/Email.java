package br.janioofi.msemail.domain.entities;

import br.janioofi.msemail.domain.enums.StatusEmail;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Email implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id_email;

    private String emailFrom;
    private String emailTo;
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String text;
    private LocalDateTime sendDateEmail;

    @Enumerated(EnumType.STRING)
    private StatusEmail statusEmail;
}
