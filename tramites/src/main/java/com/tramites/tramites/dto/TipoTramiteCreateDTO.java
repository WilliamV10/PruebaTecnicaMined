package com.tramites.tramites.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * DTO para la creación de un tipo de trámite.
 */
@Schema(description = "Datos requeridos para crear un tipo de trámite")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoTramiteCreateDTO {

    @Schema(description = "Nombre descriptivo del tipo de trámite", example = "Solicitud de Partida de Nacimiento", required = true)
    @NotBlank(message = "El nombre del tipo de trámite es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres")
    private String nombre;
}
