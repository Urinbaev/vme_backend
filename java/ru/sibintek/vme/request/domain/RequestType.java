package ru.sibintek.vme.request.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Тип заявки
 */
@Getter
@AllArgsConstructor
public enum RequestType {
  CARGO("Перевозка груза"),
  PASSENGER("Перевозка пассажиров"),
  SPECIAL("Специальная работа"),;

  private final String descr;
}
