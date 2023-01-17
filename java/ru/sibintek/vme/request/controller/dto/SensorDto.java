package ru.sibintek.vme.request.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

@Value
@Schema(description = "Датчик")
public class SensorDto {

    @Schema(description = "Идентификатор датчика")
    Integer idSensor;
    @Schema(description = "Идентификатор терминала")
    Integer idAt;
    @Schema(description = "Тип датчика")
    Integer idSensorType;
    /**
     * CLASS_IGNITION       - зажигание
     * CLASS_ON             - включение
     * CLASS_TOP_DEVICE     - включение верхнего оборудования
     * CLASS_WORK_SENSOR    - включение рабочего органа
     * CLASS_ENGINE_HOUR    - моточасы
     * CLASS_RPM            - обороты двигателя
     */
    @Schema(description = "Класс датчика")
    String sensorClass;
    @Schema(description = "Номер датчика")
    Integer sensNum;
    @Schema(description = "Тревожный датчик")
    Boolean isAlarm;
    @Schema(description = "Описание датчика")
    String description;
    @Schema(description = "Активный")
    Boolean active;
    @Schema(description = "Наименование топливного бака")
    String tankSensor;
    @Schema(description = "Серийный номер")
    String serialNumber;
    @Schema(description = "Номер пломбы 1")
    String sealNumber1;
    @Schema(description = "Номер пломбы 2")
    String sealNumber2;
    @Schema(description = "Номера зависимых датчиков")
    String sensNums;
    @Schema(description = "Наименование оборудования")
    String equip;
    @Schema(description = "Внутренний идентификатор оборудования")
    Integer idEquip;
    @Schema(description = "Период сглаживания")
    Integer smoothPeriod;
    @Schema(description = "Дата установки (dd.mm.yyyy)")
    String dateInst;
    @Schema(description = "Тип ДУЖ")
    String llsType;
    @Schema(description = "Статус при включении")
    String statusOn;
    @Schema(description = "Статус при выключении")
    String statusOff;
    @Schema(description = "Отображать статус при включении")
    Boolean statusOnView;
    @Schema(description = "Отображать статус при выключении")
    Boolean statusOffView;
    @Schema(description = "Номинальное значение")
    Double nominalValue;
    @Schema(description = "Максимальное значение пропускной способности датчика, л/ч")
    Double maxFuelSpeed;
    @Schema(description = "Не учитывать время ожидания при расчете времени работы датчика")
    Boolean skipWaitingTime;
}