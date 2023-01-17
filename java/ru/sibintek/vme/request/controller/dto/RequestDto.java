package ru.sibintek.vme.request.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.sibintek.vme.request.domain.RequestStatus;
import ru.sibintek.vme.request.domain.RequestType;
import ru.sibintek.vme.vehicle.dto.VehicleDto;
import ru.sibintek.vme.vehicle.dto.VehicleTypeDto;

import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * Модель для внешних заявок.
 */
@Data
@Schema(description = "Модель для внешних заявок.")
public class RequestDto {
    @Schema(description = "Идентификатор заявки")
    private Long id;
    @Schema(description = "Статус заявки")
    private RequestStatus status;
    @Schema(description = "Причина смены статуса")
    private String reason;
    @Schema(description = "порядковый номер задания в заявке на стороне")
    private int number;
    @Schema(description = "Номер задания")
    private int subNumber;
    @Schema(description = "Дата заявки")
    private Date date;
    @Schema(description = "uid")
    private String uid;
    @Schema(description = "Маршрут")
    private String route;
    @Schema(description = "Тип заявки (CARGO, PASSENGER, SPECIAL)")
    private RequestType type;
    @Schema(description = "Наименование и характер груза")
    private String typeOfWork;
    @Schema(description = "Детализация вида строительной техники (вносится вручную заказчиком)")
    private String typeMachine;
    @Schema(description = "Комментарий, указанный заказчиком в задании")
    private String comment;
    @Schema(description = "габариты ШхВхГ, м")
    private double[] dimensions;
    @Schema(description = "Вес, тонн")
    private Double weightCargo;
    @Schema(description = "Объем, м3")
    private Double volumeCargo;
    @Schema(description = "Мест")
    private String amount;
    @Schema(description = "Количество ТС")
    private Integer countTs;
    @Schema(description = "ТС'ы")
    private Set<VehicleDto> vehicles;
    @Schema(description = "Строковое описание \"типа ТС\" по текстовому справочнику заказчика")
    private String kindType;
    @Schema(description = "Количество ездок")
    private Integer cntTrip;
    @Schema(description = "Наименование работ")
    private String workInfo;
    @Schema(description = "Информация о пассажирах")
    private String passengerInfo;
    @Schema(description = "Заказчик")
    private CustomerDto customer;
    @Schema(description = "dateSend")
    private Date dateSend;
    @Schema(description = "dateReceiving")
    private Date dateReceiving;
    @Schema(description = "Компания")
    private int organization;
    @Schema(description = "customerApp")
    private String customerApp;
    @Schema(description = "Плановая дата начала работ")
    private Date dateStart;
    @Schema(description = "Плановая дата завершения работ.")
    private Date dateFinish;
    @Schema(description = "idOwnCustomer")
    private Integer idOwnCustomer;
    @Schema(description = "Отчетная дата, по которой будет определяться месяц, за который будет выставлен акт по данным реестра выполненных работ")
    private Date reportDate;
    @Schema(description = "Расширенные опции задания")
    private Map<String, Object> additionalOptions;
    @Schema(description = "Фактические данные")
    private FactDataDto factData;
    @Schema(description = "Дата создания")
    private Date dateCreated;
    @Schema(description = "Тип ТС")
    private VehicleTypeDto vehicleType;
}
