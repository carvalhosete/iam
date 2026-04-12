package br.com.project.iam.dto;

import jakarta.validation.constraints.NotBlank;

public record AutenticacaoDTO(

        @NotBlank(message = "Por favor, digite o e-mail para login!")
        String email,

        @NotBlank(message = "Digite a senha para realizar o login!")
        String senha
) {
}
