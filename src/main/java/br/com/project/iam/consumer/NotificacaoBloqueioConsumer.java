package br.com.project.iam.consumer;

import io.awspring.cloud.sqs.annotation.SqsListener;
import org.springframework.stereotype.Component;


@Component
public class NotificacaoBloqueioConsumer {

    @SqsListener("fila-notificacao-bloqueio")
    public void receberNotificacao(String emailBloqueado){

        System.out.println("========================================");
        System.out.println("NOVA MENSAGEM RECEBIDA NA AMAZON SQS!");
        System.out.println("Usuário bloqueado: " + emailBloqueado);
        System.out.println("Simulando envio de e-mail de recuperação....");
        System.out.println("========================================");
    }
}
