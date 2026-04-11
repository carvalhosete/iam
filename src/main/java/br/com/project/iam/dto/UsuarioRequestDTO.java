package br.com.project.iam.dto;

import jakarta.validation.constraints.NotBlank;

public record UsuarioRequestDTO(

        @NotBlank(message="O NOME do usuário é de preenchimento OBRIGATÓRIO.")
        String nome,

        @NotBlank(message = "O E-MAIL do usuário é de preenchimento OBRIGATÓRIO.")
        String email,

        @NotBlank(message ="A SENHA do usuário é de preenchimento OBRIGATÓRIO.")
        String senha
    )
{}
