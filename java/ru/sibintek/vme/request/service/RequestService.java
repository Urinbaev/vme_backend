package ru.sibintek.vme.request.service;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.sibintek.vme.request.controller.dto.AssignTSRequestDto;
import ru.sibintek.vme.request.controller.dto.CancelNotAcceptDto;
import ru.sibintek.vme.request.controller.dto.FactDataDto;
import ru.sibintek.vme.request.controller.specification.RequestCollectionSpecification;
import ru.sibintek.vme.request.domain.RequestBaseEntity;
import ru.sibintek.vme.request.domain.RequestStatus;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * Сервис, который содержит бизнес логику с {@link RequestBaseEntity}.
 */
public interface RequestService {
    String EXPORT_SHEET_TITLE = "Внешние заявки";

    /**
     * Добавление внешней заявки
     *
     * @param request - внешняя заявка, которую требуется добавить
     * @param objectUid - идентификатор внешней заявки
     * @param appUid - идентификатор заказчика
     * @param organization - организация
     */
    void put(@NonNull RequestBaseEntity request,
             @NonNull String objectUid,
             @NonNull String appUid,
             @NonNull Long organization);

    /**
     * Список заявок.
     *
     * @param search                фильтр.
     * @param createdDateFromFilter дата создания заявки - "от"
     * @param createdDateToFilter дата создания заявки - "до"
     * @param state статус заявки
     * @param filter тип ТС
     * @param pageable              {@link Pageable}.
     * @return instance of {@link Page <RequestBaseEntity>}
     */
    Page<RequestBaseEntity> getList(String search,
                                    Long filter,
                                    LocalDateTime createdDateFromFilter,
                                    LocalDateTime createdDateToFilter,
                                    RequestStatus state,
                                    Pageable pageable);

    /**
     * Чтение заявки.
     *
     * @param id идентификатор заявки.
     * @return класс {@link RequestBaseEntity}.
     */
    RequestBaseEntity getItem(Long id);

    /**
     * Сохранить успешное назначение транспорта
     *
     * @param objectUid - идентификатор внешней заявки
     */
    void saveTsOkAssignment(@NonNull String objectUid);

    /**
     * Взять внешнюю заявку в работу
     *
     * @param objectUid - идентификатор внешней заявки
     */
    void toWork(@NonNull String objectUid);

    /**
     * Заявка на снятие внешней заявки.
     *
     * @param objectUid - идентификатор внешней заявки
     * @param message   - причина снятия заявки
     */
    void toCanceled(@NonNull String objectUid, String message);

    /**
     * Запрос на переназначение ТС.
     *
     * @param objectUid  - идентификатор внешней заявки
     * @param reason     - причина переназначения
     */
    void appointNotAccept(@NonNull String objectUid, String reason);

    /**
     * Согласовать снятие внешней заявки.
     *
     * @param id - идентификатор внешней заявки
     */
    RequestBaseEntity acceptCancel(@NonNull Long id);

    /**
     * Подтвердить снятие внешней заявки.
     *
     * @param objectUid - идентификатор внешней заявки
     */
    void toCancelOk(@NonNull String objectUid);

    /**
     * Отклонить снятие внешней заявки.
     *
     * @param id - идентификатор внешней заявки
     * @param dto - структура отказа отмены заявки
     */
    RequestBaseEntity notAcceptCancel(@NonNull Long id, @NonNull CancelNotAcceptDto dto);

    /**
     * Завершить внешнюю заявку
     *
     * @param objectUid - идентификатор внешней заявки
     */
    void complete(@NonNull String objectUid);

    /**
     * Закрытие внешней заявки.
     *
     * @param objectUid идентификатор внешней заявки.
     */
    void delete(@NonNull String objectUid);

    /**
     * Сохранение фактических данных заявки.
     *
     * @param request   -Фактические данные заявки.
     * @param objectUid -идентификатор внешней заявки.
     * @param mapper    -mapper.
     */
    void putFactData(FactDataDto request, String objectUid, BiConsumer<RequestBaseEntity, FactDataDto> mapper);

    /**
     * Назначение ТС на заявку.
     *
     * @param dtos              транспортные средства.
     * @param requestBaseEntity внешняя заявка.
     */
    void assignTs(List<@Valid AssignTSRequestDto> dtos, RequestBaseEntity requestBaseEntity);

    /**
     * Выгрузка данных по внешним заявкам в excel.
     *
     * @param spec      - спецификация фильтр
     * @param sort      - сортировка
     * @param response  - ответ, в который выгружаются данные
     */
    void export(
            RequestCollectionSpecification spec,
            @NonNull Sort sort,
            @NonNull HttpServletResponse response);
}
