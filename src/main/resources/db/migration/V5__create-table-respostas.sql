CREATE TABLE respostas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    mensagem TEXT NOT NULL,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    autor_id BIGINT,
    topico_id BIGINT,
    solucao BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (autor_id) REFERENCES usuarios(id) ON DELETE SET NULL,
    FOREIGN KEY (topico_id) REFERENCES topicos(id) ON DELETE CASCADE
);