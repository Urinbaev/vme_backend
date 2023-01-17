package ru.sibintek.vme.common.config.amqp;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.sibintek.vme.common.config.properties.MqProperties;

@Configuration
@RequiredArgsConstructor
public class RabbitConfig {
    private final MqProperties mqProperties;
    
    @Bean
    public Queue incomingQueue() {
        return new Queue(mqProperties.getQueue().getIncoming(), true);
    }

    @Bean
    public Queue outcomingQueue() {
        return new Queue(mqProperties.getQueue().getOutcoming(), true);
    }

    @Bean
    public DirectExchange outcomingExchange() {
        return new DirectExchange(mqProperties.getExchange().getOutcoming());
    }

    @Bean
    public Binding outcomingBinding(Queue outcomingQueue, DirectExchange outcomingExchange) {
        return BindingBuilder.bind(outcomingQueue).to(outcomingExchange).with(mqProperties.getRk().getOutcoming());
    }
}
