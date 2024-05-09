CREATE TABLE acesso
(
    id_acesso     UUID   NOT NULL,
    id_cliente    BIGINT NOT NULL,
    cpf           VARCHAR(255),
    data_registro TIMESTAMP WITHOUT TIME ZONE,
    tipo_registro VARCHAR(255),
    CONSTRAINT pk_acesso PRIMARY KEY (id_acesso)
);

ALTER TABLE acesso
    ADD CONSTRAINT FK_ACESSO_ON_ID_CLIENTE FOREIGN KEY (id_cliente) REFERENCES cliente (id_cliente);