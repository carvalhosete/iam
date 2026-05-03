package br.com.project.iam.service;

import br.com.project.iam.domain.Usuario;
import br.com.project.iam.dto.NotificacaoBoasVindasDTO;
import br.com.project.iam.dto.UsuarioRequestDTO;
import br.com.project.iam.dto.UsuarioResponseDTO;
import br.com.project.iam.repository.UsuarioRepository;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private SqsTemplate sqsTemplate;

    @Test
    @DisplayName("Deve salvar um usuário com sucesso e disparar evento na fila")
    void salvarUsuarioComSucesso(){

        UsuarioRequestDTO requestDTO = new UsuarioRequestDTO("Leonardo", "leo@email.com", "senha123");

        Usuario usuarioSalvo = new Usuario();
        usuarioSalvo.setId(1L);
        usuarioSalvo.setNome("Leonardo");
        usuarioSalvo.setEmail("leo@email.com");
        usuarioSalvo.setSenha("senha_criptografada");

        Mockito.when(passwordEncoder.encode("senha123")).thenReturn("senha_criptografada");
        Mockito.when(usuarioRepository.save(Mockito.any(Usuario.class))).thenReturn(usuarioSalvo);

        UsuarioResponseDTO response = usuarioService.criarUsuario(requestDTO);

        Assertions.assertNotNull(response);
        Assertions.assertEquals("leo@email.com", response.email());

        Mockito.verify(passwordEncoder, Mockito.times(1)).encode("senha123");

        Mockito.verify(usuarioRepository, Mockito.times(1)).save(Mockito.any(Usuario.class));

        Mockito.verify(sqsTemplate, Mockito.times(1)).send(
                Mockito.eq("fila-notificacao-boas-vindas"),
                Mockito.any(NotificacaoBoasVindasDTO.class)
        );
    }

}
