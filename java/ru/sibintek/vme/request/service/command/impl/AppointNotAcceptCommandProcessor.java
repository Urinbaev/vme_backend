package ru.sibintek.vme.request.service.command.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sibintek.vme.common.constant.RequestCommands;
import ru.sibintek.vme.request.service.RequestService;
import ru.sibintek.vme.request.service.command.RequestCommandProcessor;
import ru.sibintek.vme.request.service.command.message.AppointNotAcceptMessage;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppointNotAcceptCommandProcessor implements RequestCommandProcessor {
    private final RequestService requestService;
    private final ObjectMapper mapper;

    @Override
    public RequestCommands getCommandCode() {
        return RequestCommands.APPOINT_NOT_ACCEPT;
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

        final var appointNotAcceptMessage = mapper.readValue(message, AppointNotAcceptMessage.class);
        requestService.appointNotAccept(objectUid, appointNotAcceptMessage.getReason());
    }
}
