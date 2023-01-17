package ru.sibintek.vme.request.domain;

import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;

import java.util.EnumSet;

/**
 * Статусы для внешних заявок (сторона контрагента-СМТ)
 * <a href="https://drive.google.com/file/d/1NiI_pCehCiybw3agKP17EjXjcP-845gi/view?usp=sharing">схема переключения и взаимодействия статусов</a>
 */
@Getter
public enum RequestStatus {
  NEW("Новая"),
  APPOINTED_TS_SEND("Назначенный транспорт отправлен заказчику"),
  APPOINTED_TS_NOT_ACCEPTED("Транспорт не согласован клиентом"),
  APPOINTED_TS("Назначен транспорт"),
  IN_WORK("В работе"),
  REMOVE_REQUEST("Запрос на снятие заявки клиентом"),
  REMOVE("Снята"),
  REMOVE_ACCEPT("Снятие заявки согласовано"),
  SUCCESSFULLY_COMPLETED("Успешное завершение"),
  SUCCESSFULLY_COMPLETED_CLIENT("Успешное завершение"),
  DELETED_BY_CUSTOMER("Удалена заказчиком");

  static {
    APPOINTED_TS_SEND.previousStates = EnumSet.of(NEW, APPOINTED_TS_SEND, APPOINTED_TS_NOT_ACCEPTED);
    APPOINTED_TS_NOT_ACCEPTED.previousStates = EnumSet.of(APPOINTED_TS_SEND, APPOINTED_TS);
    APPOINTED_TS.previousStates = EnumSet.of(APPOINTED_TS_SEND);
    IN_WORK.previousStates = EnumSet.of(APPOINTED_TS, REMOVE_REQUEST);
    REMOVE_REQUEST.previousStates = EnumSet.of(IN_WORK);
    REMOVE.previousStates = EnumSet.of(NEW, APPOINTED_TS_SEND, APPOINTED_TS, REMOVE_REQUEST);
    REMOVE_ACCEPT.previousStates = EnumSet.of(REMOVE);
    SUCCESSFULLY_COMPLETED.previousStates = EnumSet.of(IN_WORK);
  }

  private final String descr;
  private EnumSet<RequestStatus> previousStates;

  RequestStatus(String descr) {
    this.descr = descr;
  }

  public RequestStatus transition(RequestStatus state) {
    if (CollectionUtils.isEmpty(state.previousStates)) {
      return state;
    }
    for(RequestStatus tmp: state.previousStates) {
      if (this == tmp) {
        return state;
      }
    }
    throw new IllegalArgumentException("Недопустимый статус");
  }
}
