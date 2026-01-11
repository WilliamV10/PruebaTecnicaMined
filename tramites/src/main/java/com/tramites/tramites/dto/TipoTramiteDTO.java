package com.tramites.tramites.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.UUID;

/**
 * DTO de respuesta para el catálogo de tipos de trámite.
 */
@Schema(description = "Representa un tipo de trámite gubernamental")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoTramiteDTO {

    @Schema(description = "Identificador único del tipo de trámite", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @Schema(description = "Nombre descriptivo del tipo de trámite (único)", example = "Solicitud de Partida de Nacimiento")
    private String nombre;
}
