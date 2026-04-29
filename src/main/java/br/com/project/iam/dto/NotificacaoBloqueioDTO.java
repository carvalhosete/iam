package br.com.project.iam.dto;

public record NotificacaoBloqueioDTO(
        String email,
        String motivo,
        String dataHora
) {}
