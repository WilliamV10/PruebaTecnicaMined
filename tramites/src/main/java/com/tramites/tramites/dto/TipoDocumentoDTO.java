package com.tramites.tramites.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.UUID;

/**
 * DTO de respuesta para el catálogo de tipos de documento.
 */
@Schema(description = "Representa un tipo de documento de identificación ciudadana")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoDocumentoDTO {

    @Schema(description = "Identificador único del tipo de documento", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @Schema(description = "Nombre del tipo de documento (único)", example = "DUI")
    private String nombre;
}
