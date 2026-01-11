package com.tramites.tramites.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Wrapper genérico para respuestas de la API.
 * Proporciona un formato consistente con mensaje, datos y metadatos.
 *
 * @param <T> Tipo de datos contenidos en la respuesta
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    /**
     * Indica si la operación fue exitosa
     */
    private boolean success;

    /**
     * Mensaje descriptivo del resultado de la operación
     */
    private String message;

    /**
     * Datos de la respuesta (puede ser null en operaciones de eliminación)
     */
    private T data;

    /**
     * Timestamp de la respuesta
     */
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    /**
     * Crea una respuesta exitosa con datos y mensaje.
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Crea una respuesta exitosa solo con mensaje (para operaciones sin datos de retorno).
     */
    public static <T> ApiResponse<T> success(String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Crea una respuesta de error.
     */
    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
