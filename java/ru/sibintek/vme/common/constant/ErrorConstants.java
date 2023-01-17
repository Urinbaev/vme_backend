package ru.sibintek.vme.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorConstants {

    GENERAL_ERROR("[VME-E001]", "General error occurred"),
    UNKNOWN_USER("[VME-E002]", "Unknown user"),
    WRONG_REQUEST("[VME-E003]", "Wrong client request"),
    USER_NOT_FOUND("[VME-E004]", "User was not found"),
    RECORD_NOT_FOUND("[VME-E005]", "Record was not found"),
    FEIGN_CLIENT_ERROR("[VME-E006]", "Feign client error occurred"),
    VALIDATION_ERROR("[VME-E007]", "Arguments validation errors"),
    API_ERROR("[VME-E008]", "API logic error"),
    ACCESS_DENIED("[VME-E009]", "Access denied");

    private final String code;
    private final String description;

}
