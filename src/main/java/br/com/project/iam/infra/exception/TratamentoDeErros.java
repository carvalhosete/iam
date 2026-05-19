package br.com.project.iam.infra.exception;


import jakarta.persistence.EntityNotFoundException;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;

@RestControllerAdvice
public class TratamentoDeErros {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity tratarErro404() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity tratarErro400(MethodArgumentNotValidException ex) {
        var erros = ex.getFieldErrors();
        return ResponseEntity.badRequest().body(erros.stream().map(DadosErroValidacao::new).toList());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity tratarErroBadCredentials(){
        return ResponseEntity.status(401).body(new DadosErroSimples("Usuário ou senha inválidos"));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity tratarErroAuthentication(){
        return ResponseEntity.status(401).body(new DadosErroSimples("Falha na autenticação"));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity tratarErroAcessoNegato(){
        return ResponseEntity.status(403).body(new DadosErroSimples("Acesso negado"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity tratarErro500(Exception ex){
        return ResponseEntity.status(500).body(new DadosErroSimples("Erro interno no servidor: " + ex.getLocalizedMessage()));
    }

    private record DadosErroSimples(String mensagem) {
    }

    private record DadosErroValidacao(String campo, String mensagem) {
        public DadosErroValidacao(FieldError erro){
            this(erro.getField(), erro.getDefaultMessage());
        }
    }
}
