package ru.sibintek.vme.request.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Schema(description = "Модель для отказа в отклонении заявки.")
public class CancelNotAcceptDto {
    @NotNull
    @Schema(description = "Причина отказа")
    private String reason;
}
