package ru.sibintek.vme.request.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.sibintek.vme.request.controller.dto.AssignTSRequestDto;
import ru.sibintek.vme.request.controller.mapper.RequestMapper;
import ru.sibintek.vme.request.controller.dto.RequestDto;
import ru.sibintek.vme.request.controller.specification.RequestCollectionSpecification;
import ru.sibintek.vme.request.domain.RequestStatus;
import ru.sibintek.vme.request.service.RequestService;
import ru.sibintek.vme.request.controller.dto.CancelNotAcceptDto;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Rest-контроллер для работы с внешними заявками.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/requests")
@Tag(name = "Заявки", description = "Контроллер для работы с внешними заявками")
@PreAuthorize("hasAnyRole('CLIENT', 'ADMIN', 'SUPPORT')")
public class RequestController {

    private final RequestMapper mapper;
    private final RequestService requestService;

    @GetMapping
    @Operation(summary = "Чтение списка заявок", tags = {"Заявки"})
    @ApiResponses(
            @ApiResponse(responseCode = "200",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = RequestDto.class)))))
    public Page<RequestDto> getRequestList(@Parameter(description = "Поиск по номеру заявки и по организации клиента")
                                           @RequestParam(required = false) String search,

                                           @Parameter(description = "Фильтр по категория ТС.")
                                           @RequestParam(required = false) Long filter,

                                           @Parameter(name = "created_from", description = "Фильтр по дате создания.")
                                           @RequestParam(required = false, name = "created_from")
                                           @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdDateFromFilter,

                                           @Parameter(name = "created_to", description = "Фильтр по дате создания.")
                                           @RequestParam(required = false, name = "created_to")
                                           @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdDateToFilter,

                                           @Parameter(description = "Фильтр по статусам.")
                                           @RequestParam(required = false) RequestStatus state,

                                           @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {
        var page = requestService.getList(search, filter, createdDateFromFilter, createdDateToFilter, state, pageable);
        return page.map(mapper::toDTO);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Чтение заявки.", tags = {"Заявки"})
    @ApiResponses(
            @ApiResponse(responseCode = "200",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RequestDto.class))))
    public RequestDto getItem(@Parameter(description = "Идентификатор заявки") @PathVariable Long id) {
        var request = requestService.getItem(id);
        return mapper.toDTO(request);
    }

    @PostMapping("/{id}/accept-cancel")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Подтверждение на снятие внешней заявки", tags = {"Заявки"})
    @ApiResponse(responseCode = "200")
    public ResponseEntity<RequestDto> acceptCancel(@PathVariable Long id) {
        var request = requestService.acceptCancel(id);
        return ResponseEntity.ok(mapper.toDTO(request));
    }

    @PostMapping("/{id}/not-accept-cancel")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Отклонение на снятие внешней заявки", tags = {"Заявки"})
    @ApiResponse(responseCode = "200")
    public ResponseEntity<RequestDto> notAcceptCancel(@PathVariable Long id, @Valid @RequestBody CancelNotAcceptDto dto) {
        var request = requestService.notAcceptCancel(id, dto);
        return ResponseEntity.ok(mapper.toDTO(request));
    }

    @PutMapping("/{id}/assign-ts")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Назначение транспортного средства", tags = {"Заявки"})
    @ApiResponses(@ApiResponse(responseCode = "200"))
    public void assignTs(@PathVariable Long id, @NotEmpty @RequestBody List<@Valid AssignTSRequestDto> dtos) {
        var request = requestService.getItem(id);
        requestService.assignTs(dtos, request);
    }

    @GetMapping("/export")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Выгрузка внешних запросов", tags = {"Заявки"})
    @ApiResponses(
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(mediaType = MediaType.APPLICATION_XML_VALUE)))
    public void export(
            RequestCollectionSpecification spec,
            @SortDefault(sort = {"id"}, direction = Sort.Direction.DESC) Sort sort,
            HttpServletResponse response) throws IOException {
        requestService.export(spec, sort, response);
        response.flushBuffer();
    }
}
