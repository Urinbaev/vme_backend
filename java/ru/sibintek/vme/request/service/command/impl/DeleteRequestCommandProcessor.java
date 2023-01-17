
package ru.sibintek.vme.request.service.command.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sibintek.vme.common.constant.RequestCommands;
import ru.sibintek.vme.request.service.RequestService;
import ru.sibintek.vme.request.service.command.RequestCommandProcessor;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeleteRequestCommandProcessor implements RequestCommandProcessor {
    private final RequestService requestService;

    @Override
    public RequestCommands getCommandCode() {
        return RequestCommands.DELETE_EXTERNAL_REQUEST;
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

        requestService.delete(objectUid);
    }
}
