package br.com.project.iam.security;

import br.com.project.iam.config.RabbitMQConfig;
import br.com.project.iam.domain.Usuario;
import br.com.project.iam.repository.UsuarioRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class FalhaAutenticacaoListener {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private RabbitTemplate rabbitTemplate;


    @EventListener
    public void onFalhaAutenticacao(AuthenticationFailureBadCredentialsEvent event){
        String emailTentado = (String) event.getAuthentication().getPrincipal();
        Optional<Usuario> localizaUsuario = repository.findByEmail(emailTentado);

        if(localizaUsuario.isPresent()){
            Usuario usuarioLocalizado = localizaUsuario.get();
            usuarioLocalizado.setTentativasFalhas(usuarioLocalizado.getTentativasFalhas() + 1);

            if (usuarioLocalizado.getTentativasFalhas() >= 3){
                usuarioLocalizado.setAtivo(false);
                repository.save(usuarioLocalizado);
                rabbitTemplate.convertAndSend(RabbitMQConfig.FILA_NOTIFICACAO_BLOQUEIO, emailTentado);

                return;
            }

            repository.save(usuarioLocalizado);
        }
    }
}
