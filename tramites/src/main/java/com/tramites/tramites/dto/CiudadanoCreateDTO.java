package com.tramites.tramites.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

/**
 * DTO para la creación de un ciudadano.
 * Se puede enviar tipoDocumentoId O tipoDocumentoNombre (al menos uno es requerido).
 */
@Schema(description = "Datos requeridos para registrar un nuevo ciudadano. " +
        "Puede enviar tipoDocumentoId (UUID) o tipoDocumentoNombre (ej: DUI, PASAPORTE)")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CiudadanoCreateDTO {

    @Schema(description = "ID del tipo de documento (opcional si envía tipoDocumentoNombre)", 
            example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID tipoDocumentoId;

    @Schema(description = "Nombre del tipo de documento (opcional si envía tipoDocumentoId). " +
            "Valores comunes: DUI, PASAPORTE, NIT", 
            example = "DUI")
    @Size(max = 100, message = "El nombre del tipo de documento no puede exceder los 100 caracteres")
    private String tipoDocumentoNombre;

    @Schema(description = "Número del documento de identificación", example = "12345678-9", required = true)
    @NotBlank(message = "El número de documento es obligatorio")
    @Size(max = 30, message = "El número de documento no puede exceder los 30 caracteres")
    private String numeroDocumento;

    @Schema(description = "Nombre completo del ciudadano (solo letras y espacios)", example = "Juan Carlos Pérez López", required = true)
    @NotBlank(message = "El nombre del ciudadano es obligatorio")
    @Size(max = 150, message = "El nombre no puede exceder los 150 caracteres")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s]+$", message = "El nombre solo debe contener letras y espacios")
    private String nombre;

    @Schema(description = "Correo electrónico del ciudadano", example = "juan.perez@email.com", required = true)
    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "El formato del correo electrónico no es válido")
    @Size(max = 150, message = "El correo no puede exceder los 150 caracteres")
    private String correo;
}
