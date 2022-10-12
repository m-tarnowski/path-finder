package pl.kurs.pathfinder.error;

import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.kurs.pathfinder.exceptions.IllegalPlaceException;
import pl.kurs.pathfinder.exceptions.RoadNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleMethodArgumentNotValidException(MethodArgumentNotValidException exc) {
        List<ValidationErrorDto> errorDtos = exc.getAllErrors().stream()
                .map(fe -> new ValidationErrorDto((fe instanceof FieldError) ? (((FieldError) fe).getField()) : null, fe.getDefaultMessage()))
                .collect(Collectors.toList());
        return new ResponseEntity(errorDtos, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalPlaceException.class)
    public final ResponseEntity handleRecordNotFoundException(IllegalPlaceException ex) {
        return new ResponseEntity(new ValidationMessageDto(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RoadNotFoundException.class)
    public final ResponseEntity handleRecordNotFoundException(RoadNotFoundException ex) {
        return new ResponseEntity(new ValidationMessageDto(ex.getMessage()), HttpStatus.NOT_FOUND);
    }


    @Value
    static class ValidationErrorDto {
        String field;
        String code;
    }

    @Value
    static class ValidationMessageDto {
        String message;
    }

}
