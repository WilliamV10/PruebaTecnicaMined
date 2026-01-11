package com.tramites.tramites.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.UUID;

/**
 * DTO de respuesta para ciudadanos registrados.
 */
@Schema(description = "Representa un ciudadano registrado en el sistema gubernamental")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CiudadanoDTO {

    @Schema(description = "Identificador único del ciudadano", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @Schema(description = "Tipo de documento de identificación del ciudadano")
    private TipoDocumentoDTO tipoDocumento;

    @Schema(description = "Número del documento de identificación (solo dígitos)", example = "123456789")
    private String numeroDocumento;

    @Schema(description = "Nombre completo del ciudadano", example = "Juan Carlos Pérez López")
    private String nombre;

    @Schema(description = "Correo electrónico del ciudadano", example = "juan.perez@email.com")
    private String correo;
}
