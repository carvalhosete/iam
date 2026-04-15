package br.com.project.iam.controller;

import br.com.project.iam.domain.Usuario;
import br.com.project.iam.dto.AutenticacaoDTO;
import br.com.project.iam.dto.TokenJWTDTO;
import br.com.project.iam.security.TokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class AutenticacaoController {

    private final AuthenticationManager manager;
    private final TokenService tokenService;

    @PostMapping
    public ResponseEntity<TokenJWTDTO> autenticacaoLogin(@Valid @RequestBody AutenticacaoDTO dados){
        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(dados.email(),dados.senha());

        var authentication = manager.authenticate(usernamePassword);
        var usuarioAprovado = (Usuario) Objects.requireNonNull(authentication.getPrincipal());
        var tokenJWT = tokenService.gerarToken(usuarioAprovado);

        return ResponseEntity.ok(new TokenJWTDTO(tokenJWT));
    }
}
