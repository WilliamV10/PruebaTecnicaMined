package com.tramites.tramites.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO de respuesta para trámites ciudadanos.
 */
@Schema(description = "Representa un trámite realizado por un ciudadano")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TramiteDTO {

    @Schema(description = "Identificador único del trámite", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @Schema(description = "Datos del ciudadano que realiza el trámite")
    private CiudadanoDTO ciudadano;

    @Schema(description = "Tipo de trámite solicitado")
    private TipoTramiteDTO tipoTramite;

    @Schema(description = "Estado actual del trámite", example = "PENDIENTE", allowableValues = {"PENDIENTE", "APROBADO", "RECHAZADO"})
    private String estado;

    @Schema(description = "Observaciones del trámite", example = "Documentación completa")
    private String observacion;

    @Schema(description = "Fecha en que se solicitó el trámite")
    private LocalDateTime fechaSolicitud;
}
