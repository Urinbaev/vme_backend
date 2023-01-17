package ru.sibintek.vme.request.service.command;

import lombok.NonNull;
import ru.sibintek.vme.common.constant.RequestCommands;

public interface RequestCommandProcessor {

    RequestCommands getCommandCode();

    void run(@NonNull String message, String objectUid, @NonNull String appUid, Long organization)
            throws Exception;
}
