package br.com.project.iam.controller;

import br.com.project.iam.dto.UsuarioRequestDTO;
import br.com.project.iam.dto.UsuarioResponseDTO;
import br.com.project.iam.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service){
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> criarUsuario(@Valid @RequestBody UsuarioRequestDTO usuario, UriComponentsBuilder uriBuilder){

        UsuarioResponseDTO usuarioSalvo = service.criarUsuario(usuario);
        URI uri = uriBuilder.path("/usuarios/{id}").buildAndExpand(usuarioSalvo.id()).toUri();

        return ResponseEntity.created(uri).body(usuarioSalvo);
    }
}
