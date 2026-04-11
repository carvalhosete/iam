package br.com.project.iam.dto;

import br.com.project.iam.domain.Usuario;

public record UsuarioResponseDTO(
        Long id,
        String nome,
        String email
) {
    public static UsuarioResponseDTO fromEntity(Usuario usuario){
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail()
        );
    }
}
