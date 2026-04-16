package br.com.project.iam.security;


import br.com.project.iam.domain.Usuario;
import br.com.project.iam.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class SucessoAutenticacaoListener {

    @Autowired
    UsuarioRepository repository;

    @EventListener
    public void onSucessoAutenticacao(AuthenticationSuccessEvent event){
        Usuario usuarioLogado = (Usuario) event.getAuthentication().getPrincipal();
        System.out.println("Ouvinte de sucesso rodou para o email: " + usuarioLogado.getEmail());
        usuarioLogado.setTentativasFalhas(0);

        repository.save(usuarioLogado);

    }
}
