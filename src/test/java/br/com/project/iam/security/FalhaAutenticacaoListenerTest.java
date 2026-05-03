package br.com.project.iam.security;

import br.com.project.iam.domain.Usuario;
import br.com.project.iam.dto.NotificacaoBloqueioDTO;
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
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.core.Authentication;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class FalhaAutenticacaoListenerTest {

    @InjectMocks
    private FalhaAutenticacaoListener listener;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private SqsTemplate sqsTemplate;

    @Test
    @DisplayName("Deve bloquear usuario e dispara fila SQS na terceira falha ")
    void bloquearUsuarioNaTerceiraFalha(){

        String emailAlvo = "hacker@email.com";
        Usuario usuarioQuaseBloqueado = new Usuario();
        usuarioQuaseBloqueado.setEmail(emailAlvo);
        usuarioQuaseBloqueado.setTentativasFalhas(2);
        usuarioQuaseBloqueado.setAtivo(true);

        Mockito.when(usuarioRepository.findByEmail(emailAlvo)).thenReturn(Optional.of(usuarioQuaseBloqueado));

        Authentication authMock = Mockito.mock(Authentication.class);
        Mockito.when(authMock.getPrincipal()).thenReturn(emailAlvo);

        AuthenticationFailureBadCredentialsEvent eventMock = Mockito.mock(AuthenticationFailureBadCredentialsEvent.class);
        Mockito.when(eventMock.getAuthentication()).thenReturn(authMock);

        listener.onFalhaAutenticacao(eventMock);

        Assertions.assertFalse(usuarioQuaseBloqueado.getAtivo());

        Assertions.assertEquals(3, usuarioQuaseBloqueado.getTentativasFalhas());

        Mockito.verify(usuarioRepository, Mockito.times(1)).save(usuarioQuaseBloqueado);

        Mockito.verify(sqsTemplate, Mockito.times(1)).send(
                Mockito.eq("fila-notificacao-bloqueio"),
                Mockito.any(NotificacaoBloqueioDTO.class)
        );

    }

}
