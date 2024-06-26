CREATE TABLE profissional
(
    id_profissional BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    nome            VARCHAR(255)                            NOT NULL,
    sobrenome       VARCHAR(255)                            NOT NULL,
    cpf             VARCHAR(255)                            NOT NULL,
    email           VARCHAR(255)                            NOT NULL,
    data_nascimento date,
    data_admissao   date,
    id_usuario      BIGINT,
    CONSTRAINT pk_profissional PRIMARY KEY (id_profissional)
);

ALTER TABLE profissional
    ADD CONSTRAINT uc_profissional_cpf UNIQUE (cpf);

ALTER TABLE profissional
    ADD CONSTRAINT uc_profissional_email UNIQUE (email);

ALTER TABLE profissional
    ADD CONSTRAINT uc_profissional_id_usuario UNIQUE (id_usuario);

ALTER TABLE profissional
    ADD CONSTRAINT FK_PROFISSIONAL_ON_ID_USUARIO FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario);