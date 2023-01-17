package ru.sibintek.vme.request.service.command.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sibintek.vme.common.constant.RequestCommands;
import ru.sibintek.vme.request.service.command.RequestCommandProcessor;
import ru.sibintek.vme.request.service.RequestService;

@Slf4j
@Service
@RequiredArgsConstructor
public class AssignTsOkRequestCommandProcessor implements RequestCommandProcessor {
    private final RequestService requestService;

    @Override
    public RequestCommands getCommandCode() {
        return RequestCommands.ASSIGN_TS_OK;
    }

    @Override
    @Transactional
    public void run(@NonNull String message,
                    String objectUid,
                    @NonNull String appUid,
                    Long organization) {
        if (StringUtils.isBlank(objectUid)) {
            throw new IllegalArgumentException("Неверно передан Object UID");
        }

        requestService.saveTsOkAssignment(objectUid);
    }
}
