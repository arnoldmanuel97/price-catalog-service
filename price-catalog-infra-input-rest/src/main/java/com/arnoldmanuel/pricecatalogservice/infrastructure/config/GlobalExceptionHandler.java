package com.arnoldmanuel.pricecatalogservice.infrastructure.config;

import com.arnoldmanuel.pricecatalogservice.infrastructure.exceptions.PriceNotFoundException;
import com.arnoldmanuel.pricecatalogservice.infrastructure.api.model.ErrorDTO;
import com.arnoldmanuel.pricecatalogservice.infrastructure.exceptions.InvalidFormatException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PriceNotFoundException.class)
    public ResponseEntity<ErrorDTO> handlePriceNotFoundException(PriceNotFoundException ex, HttpServletRequest request) {
        return buildErrorResponse(ex, request.getRequestURI(), HttpStatus.NOT_FOUND,
                ErrorDTO.ErrorCodeEnum.RESOURCE_NOT_FOUND);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class,
            MethodArgumentNotValidException.class,
            ServletRequestBindingException.class,
            TypeMismatchException.class,
            HttpMessageConversionException.class,
            HttpRequestMethodNotSupportedException.class,
            HttpMediaTypeException.class, InvalidFormatException.class})
    public ResponseEntity<ErrorDTO> handleBadRequestException(Exception ex, HttpServletRequest request) {
        return buildErrorResponse(ex, request.getRequestURI(), HttpStatus.BAD_REQUEST,
                ErrorDTO.ErrorCodeEnum.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handleGeneralException(Exception ex, HttpServletRequest request) {
        return buildErrorResponse(ex, request.getRequestURI(), HttpStatus.INTERNAL_SERVER_ERROR,
                ErrorDTO.ErrorCodeEnum.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorDTO> buildErrorResponse(Exception ex, String path, HttpStatus httpStatus,
                                                        ErrorDTO.ErrorCodeEnum errorCode) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setErrorMessage(ex.getMessage());
        errorDTO.setStatus(httpStatus.name());
        errorDTO.setPath(path);
        errorDTO.setErrorCode(errorCode);

        return ResponseEntity.status(httpStatus)
                .contentType(MediaType.APPLICATION_JSON)
                .body(errorDTO);
    }

}
