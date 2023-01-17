package ru.sibintek.vme.request.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import ru.sibintek.vme.common.config.amqp.RawJsonDeserializer;
import ru.sibintek.vme.common.domain.BaseEntity;
import ru.sibintek.vme.common.domain.EntityStatus;
import ru.sibintek.vme.company.domain.Company;
import ru.sibintek.vme.common.exception.ApiException;
import ru.sibintek.vme.driver.domain.Driver;
import ru.sibintek.vme.request.controller.dto.CustomerDto;
import ru.sibintek.vme.request.controller.dto.FactDataTopDto;
import ru.sibintek.vme.vehicle.domain.Vehicle;
import ru.sibintek.vme.vehicle.domain.VehicleType;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@Table(name = "dat_requests", schema = "vme")
public class RequestBaseEntity extends BaseEntity {

  /**
   * История статусов заказа
   */
  @JsonIgnore
  @OrderBy("id desc")
  @OneToMany(mappedBy = "request", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  private List<RequestHistoryEntity> stateHistories;

  /**
   * Номер заявки
   */
  @Column(name = "req_number")
  private int number;

  /**
   * Номер заявки
   */
  @Column(name = "req_sub_number")
  private int subNumber;

  /**
   * Дата заявки
   */
  @Column(name = "req_date")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
  private Date date;

  /**
   * Уникальный номер заявки для связи
   */
  @Column(name = "obj_uid")
  private String uid;

  /**
   * Маршрут для заявки
   */
  @Type(type = "jsonb")
  @Column
  @JsonDeserialize(using = RawJsonDeserializer.class)
  private String route;

  /**
   * Тип заявки
   */
  @Column(name = "req_type")
  @Enumerated(EnumType.STRING)
  private RequestType type;

  /**
   * Наименование и характер груза
   */
  @Column
  private String typeOfWork;

  /**
   * Вид техники
   */
  @Column
  private String typeMachine;

  /**
   * Описание
   */
  @Column(name = "req_comment")
  private String comment;

  /**
   * Габариты
   */
  @Type(type = "jsonb")
  @Column
  private double[] dimensions;

  /**
   * Вес, тонн
   */
  @Column(scale = 3, length = 7)
  private Double weightCargo;

  /**
   * Объем, м3
   */
  @Column
  private Double volumeCargo;

  /**
   * Мест
   */
  @Column
  private String amount;

  /**
   * Количество ТС
   */
  @Column
  private Integer countTs;

  /**
   * Тип ТС
   */
  @Column
  private String kindType;

  /**
   * Количество поездок
   */
  @Column
  public Integer cntTrip;

  /**
   * Наименование работ
   */
  @Column
  private String workInfo;

  /**
   * Информация о пассажирах
   */
  @Column
  private String passengerInfo;

  /**
   * Расширенные опции задания
   */
  @Type(type = "jsonb")
  @Column
  private Map<String, String> additionalOptions;

  /**
   * Контактный телефон
   */
  @Column
  private String customerPhone;

  /**
   * Наименование организации заказчика
   */
  @Column
  private String customerOrganization;

  /**
   * Контактное лицо
   */
  @Column
  private String customerFio;

  /**
   * Идентификатор подразделения
   */
  @Column
  private int customerId;

  /**
   * true - заказчик является внутренним в удаленной системе
   */
  @Column
  private boolean customerOwn;

  @Transient
  private CustomerDto customer;

  /**
   * Дата отправки
   */
  @Column
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm:ss Z")
  private Date dateSend = new Date();

  /**
   * Дата получения
   */
  @Column
  private Date dateReceiving;

  /**
   * Подразделение, которому принадлежит заявка
   */
  @Column
  private Long organization;

  /**
   * Идентификатор внешнего заказчика для коммуникации
   */
  @Column
  private String customerApp;

  /**
   * Дата начала работ
   */
  @Column
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm Z")
  private Date dateStart;

  /**
   * Дата завершения работ
   */
  @Column
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm Z")
  private Date dateFinish;

  /**
   * Идентификатор внутреннего заказчика.
   */
  @Column
  private Integer idOwnCustomer;

  /**
   * Дата для отчета
   */
  @Column
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
  private Date reportDate;

  /**
   * "модель, марка ТС
   */
  @Column
  private String model;
  /**
   * пробег ТС
   */
  @Column
  private Double mileage;
  /**
   * моточас ТС
   */
  @Column
  private Double motoHour;
  /**
   * машиночас
   */
  @Column
  private Double moHour;
  /**
   * количество смен
   */
  @Column
  private Integer shiftCount;
  /**
   * количество пассажиров
   */
  @Column
  private Integer passengerCount;
  /**
   * пробег с грузом
   */
  @Column
  private Double mileageCargo;
  /**
   * масса груза
   */
  @Column
  private Double massCargo;
  /**
   * Дата и время выезда из гараж/убытие из предыдущего места работ
   */
  @Column
  private String feedDate;
  /**
   * Дата и время заезда в гараж/прибытие на следующее место работ
   */
  @Column
  private String returnDate;
  /**
   * Дата и время прибытия к текущему месту работ
   */
  @Column
  private String feedPointDate;
  /**
   * Дата и время убытия из текущего места работ
   */
  @Column
  private String returnPointDate;
  /**
   * Нулевой пробег суммарный
   */
  @Column
  private Double idleMileage;
  /**
   * Нулевой моточас, суммарный
   */
  @Column
  private Double idleMoHours;
  /**
   * Идентификатор назначенного транспорта ЕКТП
   */
  @Column
  private Integer idTransportContractor;
  /**
   * Идентификатор назначенного транспорта СМТ
   */
  @Column
  private Integer idTransportExternal;
  /**
   * Фактические данные верхнего оборудования
   */
  @Type(type = "jsonb")
  @Column
  private List<FactDataTopDto> tops;

  @Setter(AccessLevel.NONE)
  @OneToMany(mappedBy = "request", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
  private Set<TsDriverEntity> vehicles;

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "company_id")
  private Company company;

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "vehicle_type_id")
  private VehicleType vehicleType;

  @JsonIgnore
  public RequestHistoryEntity getCurrentState() {
    if (CollectionUtils.isEmpty(this.stateHistories)) {
      return new RequestHistoryEntity();
    }
    return this.stateHistories.get(0);
  }

  @JsonIgnore
  public RequestHistoryEntity getPreviousState() {
    if (CollectionUtils.isEmpty(this.stateHistories) || this.stateHistories.size() == 1) {
      return null;
    }
    return this.stateHistories.get(1);
  }

  public void addStateHistory(RequestStatus status, String reason) {
    if (CollectionUtils.isEmpty(this.stateHistories)) {
      this.stateHistories = new LinkedList<>();
    } else {
      this.stateHistories.get(0).getState().transition(status);
      this.stateHistories.get(0).setStatus(EntityStatus.INACTIVE);
    }
    this.stateHistories.add(0, new RequestHistoryEntity(this, status, reason));
  }

  public void addStateHistory(RequestStatus status) {
    this.addStateHistory(status, null);
  }

  public void addVehicle(Vehicle vehicle, Driver driver) {
    if (vehicles == null) {
      vehicles = new HashSet<>();
    }
    if (vehicles.size() >= countTs) {
      throw new ApiException("Достигнуто допустимое количество назначенных ТС");
    }
    vehicles.add(
            TsDriverEntity
                    .builder()
                    .id(new RequestTsKey(this.getId(), vehicle.getId(), driver.getId()))
                    .request(this)
                    .vehicle(vehicle)
                    .build()
    );
  }

  public void clearVehicles() {
    vehicles.clear();
  }
}
