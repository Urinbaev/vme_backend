package ru.sibintek.vme.request.controller.dto;

import java.util.Date;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

@Value
@Schema(description = "Профиль терминалов бортового оборудования")
public class ProfileDto {
    @Schema(description = "Идентификатор")
    Integer id;
    @Schema(description = "Код мобильного объекта")
    Integer idMO;
    @Schema(description = "Указание текущего (активного) профиля")
    Boolean isActive;
    @Schema(description = "Начало действия")
    Date dateFrom;
    @Schema(description = "Окончание действия")
    Date dateTo;
    @Schema(description = "Описание")
    String descr;
    @Schema(description = "Массив терминалов")
    List<TerminalDto> terminals;

}
