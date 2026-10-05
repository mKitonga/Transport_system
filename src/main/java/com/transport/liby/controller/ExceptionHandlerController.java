package com.transport.liby.controller;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


@RestController
@ControllerAdvice
@RestControllerAdvice
public class ExceptionHandlerController {@ExceptionHandler({CommonRuntimeException.class})
public ResponseEntity<ApiResponse> handleErrors(CommonRuntimeException exp, Locale locale) {
    ApiResponse apiResponse = new ApiResponse(false,
            exp.getType().value(),
            Message.get(exp.getMessage(), locale)
    );
    return new ResponseEntity<>(apiResponse, HttpStatusCode.valueOf(exp.getType().value()));
}

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<ApiResponse> handle(ConstraintViolationException exp, Locale locale){

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setStatus(false);
        apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
        List<String> errorMsgs = new ArrayList<>();

        exp.getConstraintViolations().forEach(
                violation -> errorMsgs.add(Message.get(violation.getMessage(), locale))
        );

        apiResponse.setMessage(String.join( "\n", errorMsgs));

        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<EntityApiResponse<List<ObjectError>>> handleInvalidArgumentsException(MethodArgumentNotValidException exp, Locale locale) {
        List<ObjectError> objectErrors = exp.getBindingResult().getAllErrors();
        List<String> errors = new ArrayList<>();
        objectErrors.forEach(objectError -> {
            String defaultErrorMsg = objectError.getDefaultMessage();
            if (defaultErrorMsg != null) {
                errors.add(Message.get(defaultErrorMsg, locale));
            }
        });

        EntityApiResponse<List<ObjectError>> apiResponse = new EntityApiResponse<>(objectErrors);
        apiResponse.setStatus(false);
        apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
        apiResponse.setMessage( String.join("\n", errors));

        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<ApiResponse> handleErrors(HttpMessageNotReadableException exp) {
        ApiResponse apiResponse = new ApiResponse(false, ExceptionType.BAD_REQUEST.value(), exp.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<ApiResponse> handleStorageFileNotFound(StorageFileNotFoundException exp) {
        ApiResponse apiResponse = new ApiResponse(false, ExceptionType.NOT_FOUND.value(), exp.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }
}

