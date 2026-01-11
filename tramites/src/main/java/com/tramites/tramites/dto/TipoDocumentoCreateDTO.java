package com.tramites.tramites.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * DTO para la creación de un tipo de documento.
 */
@Schema(description = "Datos requeridos para crear un tipo de documento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoDocumentoCreateDTO {

    @Schema(description = "Nombre del tipo de documento", example = "DUI", required = true)
    @NotBlank(message = "El nombre del tipo de documento es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres")
    private String nombre;
}
