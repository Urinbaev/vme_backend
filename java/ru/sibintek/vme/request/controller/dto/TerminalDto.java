package ru.sibintek.vme.request.controller.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

@Value
@Schema(description = "Терминал")
public class TerminalDto {
    @Schema(description = "Идентификатор терминала")
    Integer idAt;
    @Schema(description = "Код мобильного объекта")
    Integer idMO;
    @Schema(description = "Основной")
    Boolean isMain;
    @Schema(description = "Используется")
    Boolean atActive;
    @Schema(description = "Тип АТ")
    Integer idType;
    @Schema(description = "Код АТ")
    String atCode;
    @Schema(description = "Время ожидания данных в режиме остановки")
    Integer tStop;
    @Schema(description = "Время ожидания данных в режиме движения")
    Integer tRun;
    @Schema(description = "Не учитывать пробег при выключенном зажигании")
    Boolean ignoreNoIgnitionRun;
    @Schema(description = "Подключенные камеры")
    Integer[] cams;
    @Schema(description = "Метод расчета пробега")
    Integer calcRunMethod;
    @Schema(description = "Примечание")
    String nameAt;
    @Schema(description = "Дата установки")
    String dateInst;
    @Schema(description = "Список доступных датчиков")
    List<SensorDto> sensors;


}
