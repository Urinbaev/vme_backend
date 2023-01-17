package ru.sibintek.vme.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.sibintek.vme.common.constant.ErrorConstants;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.emptyMap;
import static ru.sibintek.vme.common.constant.ErrorConstants.GENERAL_ERROR;
import static ru.sibintek.vme.common.constant.ErrorConstants.VALIDATION_ERROR;

@Slf4j
@ControllerAdvice
public class ExceptionsHandler {

    /**
     * Обработка исключений при валидации полей без использования аннотации @Valid.
     *
     * @param exception выброшенное исключение от валидатора
     * @return ответ клиентской стороне, закрывающий детали ошибки
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public HttpEntity<ErrorResponse> handleHibernateValidationException(final ConstraintViolationException exception) {
        final Function<ConstraintViolation<?>, String> keyMapper = e -> e.getPropertyPath().toString();
        final Function<ConstraintViolation<?>, List<String>> valueMapper = e -> Collections.singletonList(e.getMessage());
        final BinaryOperator<List<String>> mergeFunction = (l1, l2) -> Stream.concat(l1.stream(), l2.stream())
                .collect(Collectors.toList());

        final Map<String, List<String>> validationErrors = exception.getConstraintViolations()
                .stream()
                .collect(Collectors.toMap(keyMapper, valueMapper, mergeFunction, LinkedHashMap::new));


        return new HttpEntity<>(logAndCreateErrorResponse(VALIDATION_ERROR, exception, validationErrors));
    }

    /**
     * Обработка исключений при валидации dto/domain сущностей через аннотацию @Valid.
     *
     * @param exception выброшенное исключение от валидатора
     * @return ответ клиентской стороне, закрывающий детали ошибки
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public HttpEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        final Function<ObjectError, String> keyMapper = e -> ((FieldError) e).getField();
        final Function<ObjectError, List<String>> valueMapper = e -> Collections.singletonList(e.getDefaultMessage());
        final BinaryOperator<List<String>> mergeFunction = (l1, l2) -> Stream.concat(l1.stream(), l2.stream())
                .collect(Collectors.toList());

        final Map<String, List<String>> validationErrors = exception.getBindingResult()
                .getAllErrors()
                .stream()
                .collect(Collectors.toMap(keyMapper, valueMapper, mergeFunction, LinkedHashMap::new));

        return new HttpEntity<>(logAndCreateErrorResponse(VALIDATION_ERROR, exception, validationErrors));

    }

    /**
     * Обработка исключений при запросе к стороннему сервису.
     *
     * @param exception выброшенное сторонним сервисом
     * @return ответ клиентской стороне, закрывающий детали ошибки
     */
    @ExceptionHandler(FeignClientException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public HttpEntity<ErrorResponse> handleFeignException(final FeignClientException exception) {
        return new HttpEntity<>(logAndCreateErrorResponse(ErrorConstants.FEIGN_CLIENT_ERROR, exception, emptyMap()));
    }

    /**
     * Обработка исключения при отсутствии записи в базе данных.
     *
     * @param exception выброшенное исключение от сервиса компаний
     * @return ответ клиентской стороне, закрывающий детали ошибки
     */
    @ExceptionHandler(RecordNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public HttpEntity<ErrorResponse> handleRecordNotFoundException(final RecordNotFoundException exception) {

        return new HttpEntity<>(logAndCreateErrorResponse(ErrorConstants.RECORD_NOT_FOUND, exception, emptyMap()));
    }

    /**
     * Обработка всех остальных исключений приложения.
     *
     * @param exception любое исключение от приложения
     * @return ответ клиентской стороне, закрывающий детали ошибки
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public HttpEntity<ErrorResponse> handleRuntimeException(final Exception exception) {
        return new HttpEntity<>(logAndCreateErrorResponse(GENERAL_ERROR, exception, emptyMap()));
    }

    /**
     * Обработка логических исключений выбрасываемые системой.
     *
     * @param exception выброшенное исключение от валидатора
     * @return ответ клиентской стороне, закрывающий детали ошибки
     */
    @ExceptionHandler(ApiException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public HttpEntity<ErrorResponse> handleApiLogicException(final ApiException exception) {
        return new HttpEntity<>(logAndCreateErrorResponse(ErrorConstants.API_ERROR, exception, emptyMap()));
    }

    /**
     * Обработка исключения ограничения доступа.
     *
     * @param exception выброшенное исключение от spring security
     * @return ответ клиентской стороне, закрывающий детали ошибки
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public HttpEntity<ErrorResponse> handleAccessDeniedException(final AccessDeniedException exception) {
        return new HttpEntity<>(logAndCreateErrorResponse(ErrorConstants.ACCESS_DENIED, exception, emptyMap()));
    }

    /**
     * Логирование исключения и формирование тела ошибки для http ответа.
     *
     * @param errorConstant константа содержащая код и описание ошибки
     * @param throwable     исключение
     * @return сущность {@link  HttpEntity}.
     */
    private ErrorResponse logAndCreateErrorResponse(ErrorConstants errorConstant, final Throwable throwable,
                                                    Map<String, List<String>> validationErrors) {
        log.error(throwable.getMessage(), throwable);

        ErrorResponse.ErrorResponseBuilder builder = ErrorResponse.builder()
                .code(errorConstant.getCode())
                .name(errorConstant.name())
                .description(errorConstant.getDescription())
                .error(throwable.getMessage());

        if (!EnumSet.of(GENERAL_ERROR, VALIDATION_ERROR).contains(errorConstant)) {
            builder.error(throwable.getMessage());
        }
        if (errorConstant == VALIDATION_ERROR) {
            builder.errors(validationErrors);
        }
        return builder.build();
    }

}
