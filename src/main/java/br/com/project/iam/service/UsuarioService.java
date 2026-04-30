package br.com.project.iam.service;

import br.com.project.iam.domain.Usuario;
import br.com.project.iam.dto.NotificacaoBoasVindasDTO;
import br.com.project.iam.dto.UsuarioRequestDTO;
import br.com.project.iam.dto.UsuarioResponseDTO;
import br.com.project.iam.repository.UsuarioRepository;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final SqsTemplate sqsTemplate;

    public UsuarioResponseDTO criarUsuario(UsuarioRequestDTO usuarioDTO){

        Usuario usuarioEntity = new Usuario();
        usuarioEntity.setNome(usuarioDTO.nome());
        usuarioEntity.setEmail(usuarioDTO.email());
        usuarioEntity.setSenha(passwordEncoder.encode(usuarioDTO.senha()));

        Usuario usuarioSalvo = usuarioRepository.save(usuarioEntity);

        try{
            NotificacaoBoasVindasDTO payload = new NotificacaoBoasVindasDTO(
                    usuarioSalvo.getEmail(),
                    usuarioSalvo.getNome()
            );
            sqsTemplate.send("fila-notificacao-boas-vindas", payload);
        } catch (Exception e){
            System.out.println("Erro ao notificar fila de boas-vindas " + e.getMessage());
        }

        return UsuarioResponseDTO.fromEntity(usuarioSalvo);
    }
}
