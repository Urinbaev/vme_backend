package ru.sibintek.vme.request.service.command.impl;

import static ru.sibintek.vme.common.constant.RequestCommands.PUT_FACT_DATA;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.sibintek.vme.common.constant.RequestCommands;
import ru.sibintek.vme.request.controller.dto.FactDataDto;
import ru.sibintek.vme.request.controller.mapper.RequestMapper;
import ru.sibintek.vme.request.service.RequestService;
import ru.sibintek.vme.request.service.command.RequestCommandProcessor;

@Slf4j
@Service
@RequiredArgsConstructor
public class PutFactDataCommandProcessor implements RequestCommandProcessor {

    private final ObjectMapper mapper;
    private final RequestService requestService;
    private final RequestMapper requestMapper;

    @Override
    public RequestCommands getCommandCode() {
        return PUT_FACT_DATA;
    }

    @Override
    public void run(@NonNull String message, String objectUid, @NonNull String appUid, Long organization) throws Exception {
        if (StringUtils.isBlank(objectUid)) {
            throw new IllegalArgumentException("Неверно передан Object UID");
        }

        var request = mapper.readValue(message, FactDataDto.class);
        requestService.putFactData(request, objectUid, requestMapper::mapFactData);
    }
}
