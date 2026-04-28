package br.com.project.iam.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String FILA_NOTIFICACAO_BLOQUEIO = "fila.notificacao.bloqueio";

    @Bean
    public Queue filaBloqueio() {
        return new Queue(FILA_NOTIFICACAO_BLOQUEIO, true);
    }
}
