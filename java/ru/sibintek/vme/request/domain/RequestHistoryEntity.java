package ru.sibintek.vme.request.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.sibintek.vme.common.domain.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "dat_request_histories", schema = "vme")
public class RequestHistoryEntity extends BaseEntity {

  /**
   * Внешней заявки
   */
  @ManyToOne
  @JoinColumn(name="request_id")
  private RequestBaseEntity request;

  /**
   * Статус заявки
   */
  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private RequestStatus state = RequestStatus.NEW;

  /**
   * Причина
   */
  @Column
  private String reason;
}
