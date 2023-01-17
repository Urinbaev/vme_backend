package ru.sibintek.vme.request.service.command.message;

import lombok.Data;

@Data
public class AppointNotAcceptMessage {
    private String reason;
}
