package ru.sibintek.vme.request.service.command.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sibintek.vme.common.config.properties.MqProperties;
import ru.sibintek.vme.common.constant.MessageType;
import ru.sibintek.vme.common.constant.RequestCommands;
import ru.sibintek.vme.common.service.SenderService;
import ru.sibintek.vme.request.service.command.RequestCommandProcessor;
import ru.sibintek.vme.request.service.RequestService;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompletedClientRequestCommandProcessor implements RequestCommandProcessor {
    private final RequestService requestService;
    private final SenderService senderService;
    private final MqProperties mqProperties;

    @Override
    public RequestCommands getCommandCode() {
        return RequestCommands.COMPLETED_CLIENT;
    }

    @Override
    @Transactional
    public void run(@NonNull String message,
                    String objectUid,
                    @NonNull String appUid,
                    Long organization) throws Exception {
        if (StringUtils.isBlank(objectUid)) {
            throw new IllegalArgumentException("Неверно передан Object UID");
        }

        requestService.complete(objectUid);

        senderService.sendCommand(
                mqProperties.getExchange().getOutcoming(),
                mqProperties.getRk().getOutcoming(),
                objectUid,
                RequestCommands.COMPLETED_CONTRACTOR,
                MessageType.TEXT,
                "OK"
        );
    }
}
