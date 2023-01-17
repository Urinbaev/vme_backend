package ru.sibintek.vme.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Service;
import ru.sibintek.vme.common.constant.Constants;
import ru.sibintek.vme.request.service.command.RequestCommandProcessor;
import ru.sibintek.vme.common.util.Global;

import java.util.Base64;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class QueueRequestListener {
    private final List<RequestCommandProcessor> commandProcessors;

    @RabbitListener(queues = "${mq.queue.incoming}")
    public void receive(String message, @Headers Map<String, Object> headers) {
        final var command = Global.getStr(headers, Constants.X_COMMAND);
        final var objectUid = Global.getStr(headers, Constants.X_OBJECT_UID);
        final var appUid = Global.getStr(headers, Constants.X_APP_UID);
        final var token = Global.getStr(headers, Constants.X_TOKEN_ORG);

        if (StringUtils.isBlank(command)) {
            log.error("Неверно передан Command.");
            return;
        }

        log.info("Обработка внешней команды. {}.", command);

        if (StringUtils.isBlank(appUid)) {
            log.error("Неверно передан APP UID.");
            return;
        }

        Long organization = null;
        if (StringUtils.isNotBlank(token)) {
            organization = Global.parseLong(new String(Base64.getDecoder().decode(token)), null);
            if (organization == null) {
                log.error("Неверно передан token: " + token);
                return;
            }
        }

        final var processor = getProcessor(command);
        if (processor == null) {
            log.error("Неверно передан Command: " + command);
            return;
        }

        try {
            processor.run(message, objectUid, appUid, organization);
        } catch (Exception e) {
            log.error("Возникла ошибка во время обработки команды {}.", command, e);
        }
    }

    private RequestCommandProcessor getProcessor(String command) {
        for (RequestCommandProcessor processor : commandProcessors) {
            if (processor.getCommandCode().getValue().equals(command)) {
                return processor;
            }
        }
        return null;
    }
}
