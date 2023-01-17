package ru.sibintek.vme.request.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.sibintek.vme.common.config.properties.MqProperties;
import ru.sibintek.vme.common.constant.MessageType;
import ru.sibintek.vme.common.constant.RequestCommands;
import ru.sibintek.vme.common.exception.RecordNotFoundException;
import ru.sibintek.vme.common.security.AuthenticationFacade;
import ru.sibintek.vme.common.service.SenderService;
import ru.sibintek.vme.common.util.excel.ExcelOrganizer;
import ru.sibintek.vme.company.domain.Company;
import ru.sibintek.vme.company.service.CompanyService;
import ru.sibintek.vme.driver.domain.Driver;
import ru.sibintek.vme.driver.dto.DriverDto;
import ru.sibintek.vme.driver.service.DriverService;
import ru.sibintek.vme.request.controller.dto.AssignTSRequestDto;
import ru.sibintek.vme.request.controller.dto.CancelNotAcceptDto;
import ru.sibintek.vme.request.controller.dto.FactDataDto;
import ru.sibintek.vme.request.controller.mapper.RequestMapper;
import ru.sibintek.vme.request.controller.specification.RequestCollectionSpecification;
import ru.sibintek.vme.request.domain.RequestBaseEntity;
import ru.sibintek.vme.request.domain.RequestStatus;
import ru.sibintek.vme.request.repository.RequestRepository;
import ru.sibintek.vme.request.service.RequestService;
import ru.sibintek.vme.request.service.constant.ExportMapping;
import ru.sibintek.vme.vehicle.domain.Vehicle;
import ru.sibintek.vme.vehicle.services.VehicleService;
import ru.sibintek.vme.vehicle.services.VehicleTypeService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final ObjectMapper mapper;
    private final RequestMapper requestMapper;
    private final MqProperties mqProperties;
    private final SenderService senderService;
    private final RequestRepository requestRepository;
    private final DriverService driverService;
    private final VehicleService vehicleService;
    private final AuthenticationFacade authenticationFacade;
    private final CompanyService companyService;
    private final VehicleTypeService vehicleTypeService;

    @Override
    @Transactional
    public void put(@NonNull RequestBaseEntity request,
                    @NonNull String objectUid,
                    @NonNull String appUid,
                    @NonNull Long organization) {
        log.info("Добавление внешней заявки. {}. {}.", objectUid, request);

        if (requestRepository.findByUidAndOrganization(objectUid, organization).isPresent()) {
            log.error("Внешняя заявка уже добавлена.");
            return;
        }
        final var company = companyService.getCompanyById(organization);

        if (StringUtils.hasText(request.getKindType())) {
            try {
                final var vehicleType = vehicleTypeService.getByName(request.getKindType());
                request.setVehicleType(vehicleType);
            } catch (Exception e) {
                //ignored
            }
        }
        request.setUid(objectUid);
        request.setCustomerApp(appUid);
        request.setOrganization(organization);
        request.setCompany(company);
        request.addStateHistory(RequestStatus.NEW);
        final var customerDto = request.getCustomer();

        if (customerDto != null) {
            request.setCustomerId(customerDto.getId());
            request.setCustomerFio(customerDto.getFio());
            request.setCustomerOwn(customerDto.isOwn());
            request.setCustomerPhone(customerDto.getPhone());
            request.setCustomerOrganization(customerDto.getOrganization());
        }
        requestRepository.save(request);

        log.info("Внешняя заявка успешно добавлена.");
    }

    @Override
    @Transactional
    public void saveTsOkAssignment(@NonNull String objectUid) {
        log.info("Обновление внешней заявки после успешного назначения ТС. {}", objectUid);

        requestRepository
                .findByUid(objectUid)
                .ifPresentOrElse(
                        request -> {
                            try {
                                request.addStateHistory(RequestStatus.APPOINTED_TS);
                                requestRepository.save(request);

                                log.info("Внешняя заявка обновлена после успешного назначения ТС.");
                            } catch (IllegalArgumentException e) {
                                log.error("Невозможно обновить заявку после успешного назначения ТС в этом статусе.");
                            }
                        },
                        () -> log.error("Внешняя заявка не найдена. {}.", objectUid)
                );
    }

    @Override
    @Transactional
    public void toWork(@NonNull String objectUid) {
        log.info("Взятие внешней заявки в работу. {}", objectUid);

        requestRepository
                .findByUid(objectUid)
                .ifPresentOrElse(
                        request -> {
                            try {
                                request.addStateHistory(RequestStatus.IN_WORK);
                                requestRepository.save(request);

                                log.info("Внешняя заявка взята в работу.");
                            } catch (IllegalArgumentException e) {
                                log.error("Невозможно взять в работу внешнюю заявку в этом статусе.");
                            }
                        },
                        () -> log.error("Внешняя заявка не найдена. {}.", objectUid)
                );
    }

    @Override
    @Transactional
    public void toCanceled(@NonNull String objectUid, String message) {
        log.info("Запрос на снятие внешней заявки. {}", objectUid);

        requestRepository
                .findByUid(objectUid)
                .ifPresentOrElse(
                        request -> {
                            try {
                                boolean inWork = request.getCurrentState().getState() == RequestStatus.IN_WORK;
                                var newState = inWork ? RequestStatus.REMOVE_REQUEST : RequestStatus.REMOVE;

                                request.addStateHistory(newState, message);
                                requestRepository.save(request);

                                if (!inWork) {
                                    senderService.sendCommand(
                                            mqProperties.getExchange().getOutcoming(),
                                            mqProperties.getRk().getOutcoming(),
                                            request.getUid(),
                                            RequestCommands.CANCEL_ACCEPT,
                                            MessageType.TEXT,
                                            "NOT_CONFIRM"
                                    );
                                }

                                log.info("Внешняя заявка снята.");
                            } catch (IllegalArgumentException e) {
                                log.error("Невозможно снять внешнюю заявку в этом статусе.");
                            }
                        },
                        () -> log.error("Внешняя заявка не найдена. {}.", objectUid)
                );
    }

    @Override
    @Transactional
    public void appointNotAccept(@NonNull String objectUid, String reason) {
        log.info("Запрос на переназначение ТС. {}", objectUid);

        requestRepository
                .findByUid(objectUid)
                .ifPresentOrElse(
                        request -> {
                            try {
                                request.addStateHistory(RequestStatus.APPOINTED_TS_NOT_ACCEPTED, reason);
                                requestRepository.save(request);

                                log.info("Внешняя заявка требует переназначения ТС.");
                            } catch (IllegalArgumentException e) {
                                log.error("Невозможно оставить заявку на переназначение ТС в этом статусе.");
                            }
                        },
                        () -> log.error("Внешняя заявка не найдена. {}.", objectUid)
                );
    }

    @Override
    @Transactional
    public RequestBaseEntity acceptCancel(@NonNull Long id) {
        log.info("Согласование снятия внешней заявки. {}", id);

        var request = requestRepository
                .findById(id)
                .orElseThrow(() -> new RecordNotFoundException(
                        MessageFormat.format("Внешняя заявка не найдена. {0}", id)
                ));

        request.addStateHistory(RequestStatus.REMOVE);
        requestRepository.save(request);

        senderService.sendCommand(
                mqProperties.getExchange().getOutcoming(),
                mqProperties.getRk().getOutcoming(),
                request.getUid(),
                RequestCommands.CANCEL_ACCEPT,
                MessageType.TEXT,
                "OK"
        );

        log.info("Снятие внешней заявки согласовано.");
        return request;
    }

    @Override
    @Transactional
    public void toCancelOk(@NonNull String objectUid) {
        log.info("Подтверждение на снятие внешней заявки. {}", objectUid);

        requestRepository
                .findByUid(objectUid)
                .ifPresentOrElse(
                        request -> {
                            try {
                                request.addStateHistory(RequestStatus.REMOVE_ACCEPT);
                                requestRepository.save(request);

                                log.info("Снятие внешней заявки подтверждена.");
                            } catch (IllegalArgumentException e) {
                                log.error("Невозможно подтвердить снятие внешней заявки в этом статусе.");
                            }
                        },
                        () -> log.error("Внешняя заявка не найдена. {}.", objectUid)
                );
    }

    @Override
    @Transactional
    public RequestBaseEntity notAcceptCancel(@NonNull Long id, @NonNull CancelNotAcceptDto dto) {
        log.info("Отклонение на снятие внешней заявки. {}", id);

        var request = requestRepository
                .findById(id)
                .orElseThrow(() -> new RecordNotFoundException(
                        MessageFormat.format("Внешняя заявка не найдена. {0}", id)
                ));

        request.addStateHistory(request.getPreviousState().getState(), dto.getReason());
        requestRepository.save(request);

        senderService.sendCommand(
                mqProperties.getExchange().getOutcoming(),
                mqProperties.getRk().getOutcoming(),
                request.getUid(),
                RequestCommands.CANCEL_NOT_ACCEPT,
                MessageType.JSON,
                dto
        );

        log.info("Снятие внешней заявки отклонена.");
        return request;
    }

    @Override
    @Transactional
    public void complete(@NonNull String objectUid) {
        log.info("Завершение внешней заявки клиентом. {}", objectUid);

        requestRepository
                .findByUid(objectUid)
                .ifPresentOrElse(
                        request -> {
                            try {
                                request.addStateHistory(RequestStatus.SUCCESSFULLY_COMPLETED);
                                requestRepository.save(request);

                                log.info("Внешняя заявка завершена клиентом.");
                            } catch (IllegalArgumentException e) {
                                log.error("Невозможно завершить внешнюю заявку в этом статусе.");
                                throw e;
                            }
                        },
                        () -> log.error("Внешняя заявка не найдена. {}.", objectUid)
                );
    }

    @Override
    @Transactional
    public void delete(@NonNull String objectUid) {
        log.info("Удаление внешней заявки клиентом. {}", objectUid);

        requestRepository
                .findByUid(objectUid)
                .ifPresentOrElse(
                        request -> {
                            try {
                                request.addStateHistory(RequestStatus.DELETED_BY_CUSTOMER);
                                requestRepository.save(request);

                                log.info("Внешняя заявка удалена клиентом.");
                            } catch (IllegalArgumentException e) {
                                log.error("Невозможно удалить внешнюю заявку в этом статусе.");
                                throw e;
                            }
                        },
                        () -> log.error("Внешняя заявка не найдена. {}.", objectUid)
                );
    }

    @Override
    @Transactional
    public void putFactData(FactDataDto factDataDto, String objectUid, BiConsumer<RequestBaseEntity, FactDataDto> mapper) {
        log.info("Сохранение фактических данных внешней заявки {}", factDataDto);

        requestRepository.findByUid(objectUid)
                .ifPresentOrElse(
                        request -> {
                            try {
                                mapper.accept(request, factDataDto);
                                requestRepository.save(request);
                                log.info("Сохранение фактических данных внешней заявки.");
                            } catch (IllegalArgumentException e) {
                                log.error("Невозможно сохранить внешние фактические данные заявки.");
                                throw e;
                            }
                        },
                        () -> {
                            log.error("Внешняя заявка не найдена. {}.", objectUid);
                            throw new RecordNotFoundException("Внешняя заявка не найдена.");
                        }
                );
    }

    @Override
    @SneakyThrows
    @Transactional
    public void assignTs(List<@Valid AssignTSRequestDto> dtos, RequestBaseEntity request) {
        if (request.getCurrentState().getState() == RequestStatus.APPOINTED_TS_NOT_ACCEPTED) {
            request.clearVehicles();
        }

        request.addStateHistory(RequestStatus.APPOINTED_TS_SEND);

        var assignTSDtos = dtos.stream().map(dto -> {
            var driverId = dto.getDriverId();

            Driver driver = null;
            if (Objects.nonNull(driverId)) {
                driver = driverService.getDriverById(driverId);
            }

            if (Objects.isNull(driver)) {
                DriverDto driverDto = new DriverDto();
                driverDto.setFio(dto.getDriverFio());
                driverDto.setContactInfo(dto.getDriverContactInfo());
                driver = driverService.addDriver(authenticationFacade.getCurrentUser().getCompanyId(), driverDto);
            }

            Vehicle vehicle;
            if (request.getVehicleType() != null) {
                vehicle = vehicleService.getByIdAndType(dto.getTsId(), request.getVehicleType());
            } else {
                vehicle = vehicleService.getById(dto.getTsId());
            }
            vehicle.addDriver(driver);
            request.addVehicle(vehicle, driver);

            var assignTSDto = requestMapper.mapAssignTs(dto, vehicle, request);

            assignTSDto.setMark(assignTSDto.getModel());
            assignTSDto.setDriverFio2(assignTSDto.getDriverFio());
            return assignTSDto;
        }).collect(Collectors.toList());

        requestRepository.save(request);

        senderService.sendCommand(
                mqProperties.getExchange().getOutcoming(),
                mqProperties.getRk().getOutcoming(),
                request.getUid(),
                RequestCommands.ASSIGN_TS,
                MessageType.JSON,
                assignTSDtos
        );
    }

    @Override
    @Transactional(readOnly = true)
    public void export(
            RequestCollectionSpecification spec,
            @NonNull Sort sort,
            @NonNull HttpServletResponse response) {
        Specification<RequestBaseEntity> ownerSpec = (r, q, c) -> c.equal(
                r.get("company").get("id"),
                authenticationFacade.getCurrentUser().getCompanyId());

        var requests = requestRepository.findAll(Specification.where(spec).and(ownerSpec), sort);
        var mappedRequests = requests
                .stream()
                .map(request -> mapper.convertValue(request, new TypeReference<Map<String, Object>>() {}))
                .collect(Collectors.toList());

        ExcelOrganizer.writeToResponse(EXPORT_SHEET_TITLE, ExportMapping.EXPORT_FIELDS_SET, mappedRequests, response);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RequestBaseEntity> getList(String search,
                                           Long filter,
                                           LocalDateTime createdDateFromFilter,
                                           LocalDateTime createdDateToFilter,
                                           RequestStatus state,
                                           Pageable pageable) {
        Company company = companyService.getOwnerCompany();
        try {
            return requestRepository.getList(
                    (state == null) ? List.of(RequestStatus.values()) : List.of(state),
                    search,
                    filter,
                    createdDateFromFilter,
                    createdDateToFilter,
                    company,
                    pageable
            );
        } catch (Exception e) {
            log.error("Ошибка чтение списка заявок: {}", e, e);
        }
        return Page.empty();
    }

    @Override
    @Transactional(readOnly = true)
    public RequestBaseEntity getItem(Long id) {
        return requestRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Заявка с идентификатором %s не найдена", id)));
    }
}
