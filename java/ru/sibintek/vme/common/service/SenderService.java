package ru.sibintek.vme.common.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import ru.sibintek.vme.common.config.properties.MqProperties;
import ru.sibintek.vme.common.constant.Constants;
import ru.sibintek.vme.common.constant.MessageType;
import ru.sibintek.vme.common.constant.RequestCommands;

@Service
@RequiredArgsConstructor
public class SenderService {
    private final RabbitTemplate rabbitTemplate;
    private final MqProperties mqProperties;
    private final ObjectMapper mapper;

    @SneakyThrows
    public void sendCommand(
            String exchange, String rk, String objectId, RequestCommands command, MessageType type, Object message) {
        final var properties = new MessageProperties();
        properties.setType(String.valueOf(ObjectUtils.defaultIfNull(type, MessageType.JSON)));
        properties.setHeader(Constants.X_COMMAND, command.getValue());
        properties.setHeader(Constants.X_API_VERSION, mqProperties.getApiVersion());
        properties.setHeader(Constants.X_APP_UID, mqProperties.getAppUid());
        properties.setHeader(Constants.X_OBJECT_UID, objectId);
        properties.setContentType("application/json;charset=utf-8");

        final var msg = new Message(mapper.writeValueAsBytes(message), properties);
        rabbitTemplate.send(exchange, rk, msg);
    }
}
