package com.tramites.tramites.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Estructura estándar para respuestas de error de la API.
 */
@Schema(description = "Respuesta estándar de error del sistema")
@Getter
@Setter
@Builder
public class ErrorResponse {

    @Schema(description = "Marca de tiempo del error", example = "2024-01-15T10:30:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    @Schema(description = "Código de estado HTTP", example = "404")
    private int status;

    @Schema(description = "Descripción del tipo de error", example = "Not Found")
    private String error;

    @Schema(description = "Mensaje descriptivo del error", example = "Ciudadano no encontrado")
    private String message;

    @Schema(description = "Ruta del endpoint que generó el error", example = "/api/v1/ciudadanos/123")
    private String path;

    @Schema(description = "Lista de errores de validación (opcional)")
    private List<ValidationError> validationErrors;

    /**
     * Representa un error de validación específico de un campo.
     */
    @Schema(description = "Error de validación de campo")
    @Getter
    @Setter
    @Builder
    public static class ValidationError {
        
        @Schema(description = "Nombre del campo con error", example = "nombre")
        private String field;
        
        @Schema(description = "Mensaje de error del campo", example = "El nombre es obligatorio")
        private String message;
    }
}
