package com.tramites.tramites.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

/**
 * DTO para la creación de un trámite.
 * Se puede enviar tipoTramiteId O tipoTramiteNombre (al menos uno es requerido).
 */
@Schema(description = "Datos requeridos para registrar un nuevo trámite. " +
        "Puede enviar tipoTramiteId (UUID) o tipoTramiteNombre (ej: LICENCIA_CONDUCIR)")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TramiteCreateDTO {

    @Schema(description = "ID del ciudadano que realiza el trámite", 
            example = "550e8400-e29b-41d4-a716-446655440000", required = true)
    @NotNull(message = "El ciudadano es obligatorio")
    private UUID ciudadanoId;

    @Schema(description = "ID del tipo de trámite (opcional si envía tipoTramiteNombre)", 
            example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID tipoTramiteId;

    @Schema(description = "Nombre del tipo de trámite (opcional si envía tipoTramiteId). " +
            "Valores comunes: LICENCIA_CONDUCIR, PASAPORTE, REGISTRO_CIVIL", 
            example = "LICENCIA_CONDUCIR")
    @Size(max = 100, message = "El nombre del tipo de trámite no puede exceder los 100 caracteres")
    private String tipoTramiteNombre;

    @Schema(description = "Observaciones del trámite (opcional)", example = "Urgente")
    @Size(max = 500, message = "La observación no puede exceder los 500 caracteres")
    private String observacion;
}
