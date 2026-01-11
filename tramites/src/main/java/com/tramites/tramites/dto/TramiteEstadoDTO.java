package com.tramites.tramites.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * DTO para actualizar el estado de un trámite.
 */
@Schema(description = "Datos para actualizar el estado de un trámite")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TramiteEstadoDTO {

    @Schema(description = "Nuevo estado del trámite", 
            example = "APROBADO", 
            required = true,
            allowableValues = {"PENDIENTE", "APROBADO", "RECHAZADO"})
    @NotBlank(message = "El estado es obligatorio")
    @Pattern(regexp = "^(PENDIENTE|APROBADO|RECHAZADO)$", 
             message = "El estado debe ser: PENDIENTE, APROBADO o RECHAZADO")
    private String estado;

    @Schema(description = "Observación sobre el cambio de estado (opcional)", 
            example = "Documentación verificada correctamente")
    @Size(max = 500, message = "La observación no puede exceder los 500 caracteres")
    private String observacion;
}
