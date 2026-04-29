package br.com.project.iam.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarEmailBloqueio(String destinatario){
        SimpleMailMessage mensagem = new SimpleMailMessage();

        mensagem.setTo(destinatario);
        mensagem.setSubject("Alerta de Segurança: Conta Bloqueada");
        mensagem.setText("Olá!\n\n" +
                "Detectamos múltiplas tentativas de login falhas na sua conta. " +
                "Por motivos de segurança, o seu acesso foi temporariamente bloqueado.\n\n" +
                "Por favor, entre em contato com o administrador do sistema.");

        mailSender.send(mensagem);
        System.out.println("E-mail disparado som sucesso para o Mailtrap " + destinatario);
    }
}
