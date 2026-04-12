package br.com.project.iam.controller;

import br.com.project.iam.dto.AutenticacaoDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class AutenticacaoController {

    private final AuthenticationManager manager;

    @PostMapping
    public ResponseEntity<Void> autenticacaoLogin(@Valid @RequestBody AutenticacaoDTO dados){
        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(dados.email(),dados.senha());

        manager.authenticate(usernamePassword);

        return ResponseEntity.ok().build();
    }
}
