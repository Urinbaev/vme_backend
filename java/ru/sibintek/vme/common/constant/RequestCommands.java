package ru.sibintek.vme.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RequestCommands {
    PUT_REQUEST("putRequest"),
    REQUEST_OK("requestOk"),
    ASSIGN_TS("assignTs"),
    APPOINT_NOT_ACCEPT("appointNotAccept"),
    ASSIGN_TS_OK("assignTsOk"),
    TO_WORK("toWork"),
    TO_CANCELED("toCanceled"),
    CANCEL_ACCEPT("cancelAccept"),
    CANCEL_NOT_ACCEPT("cancelNotAccept"),
    TO_CANCEL_OK("toCancelOk"),
    PUT_FACT_DATA("putFactData"),
    COMPLETED_CLIENT("completedClient"),
    COMPLETED_CONTRACTOR("completedContractor"),
    DELETE_EXTERNAL_REQUEST("deleteExternalRequest");

    private final String value;
}
