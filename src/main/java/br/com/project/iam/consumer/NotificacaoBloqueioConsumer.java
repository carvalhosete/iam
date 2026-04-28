package br.com.project.iam.consumer;

import br.com.project.iam.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


@Component
public class NotificacaoBloqueioConsumer {

    @RabbitListener(queues = RabbitMQConfig.FILA_NOTIFICACAO_BLOQUEIO)
    public void receberNotificacao(String emailBloqueado){

        System.out.println("========================================");
        System.out.println("NOVA MENSAGEM RECEBIDA DO RABBITMQ!");
        System.out.println("Usuário bloqueado: " + emailBloqueado);
        System.out.println("Simulando envio de e-mail de recuperação....");
        System.out.println("========================================");
    }
}
