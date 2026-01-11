package com.tramites.tramites.controller;

import com.tramites.tramites.dto.ApiResponse;
import com.tramites.tramites.dto.TipoDocumentoCreateDTO;
import com.tramites.tramites.dto.TipoDocumentoDTO;
import com.tramites.tramites.exception.ErrorResponse;
import com.tramites.tramites.service.TipoDocumentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controlador REST para gestión de tipos de documento de identificación.
 * Catálogo de documentos oficiales reconocidos por la institución.
 */
@RestController
@RequestMapping("/api/v1/tipos-documento")
@RequiredArgsConstructor
@Tag(name = "Tipos de Documento", description = "API para gestión del catálogo de tipos de documento de identificación ciudadana")
public class TipoDocumentoController {

    private final TipoDocumentoService tipoDocumentoService;

    @Operation(
            summary = "Listar tipos de documento",
            description = "Obtiene todos los tipos de documento de identificación activos en el sistema"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TipoDocumentoDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<TipoDocumentoDTO>> findAll() {
        return ResponseEntity.ok(tipoDocumentoService.findAll());
    }

    @Operation(
            summary = "Obtener tipo de documento por ID",
            description = "Busca un tipo de documento específico por su identificador único"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Tipo de documento encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TipoDocumentoDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Tipo de documento no encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<TipoDocumentoDTO> findById(
            @Parameter(description = "ID del tipo de documento", required = true)
            @PathVariable UUID id) {
        return ResponseEntity.ok(tipoDocumentoService.findById(id));
    }

    @Operation(
            summary = "Crear tipo de documento",
            description = "Registra un nuevo tipo de documento en el catálogo del sistema"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Tipo de documento creado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos inválidos o tipo de documento duplicado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<ApiResponse<TipoDocumentoDTO>> create(
            @Parameter(description = "Datos del tipo de documento", required = true)
            @Valid @RequestBody TipoDocumentoCreateDTO dto) {
        TipoDocumentoDTO created = tipoDocumentoService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Tipo de documento creado exitosamente", created));
    }

    @Operation(
            summary = "Actualizar tipo de documento",
            description = "Actualiza los datos de un tipo de documento existente"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Tipo de documento actualizado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Tipo de documento no encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos inválidos",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TipoDocumentoDTO>> update(
            @Parameter(description = "ID del tipo de documento", required = true)
            @PathVariable UUID id,
            @Parameter(description = "Datos actualizados", required = true)
            @Valid @RequestBody TipoDocumentoCreateDTO dto) {
        TipoDocumentoDTO updated = tipoDocumentoService.update(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Tipo de documento actualizado exitosamente", updated));
    }

    @Operation(
            summary = "Eliminar tipo de documento",
            description = "Realiza una eliminación lógica (soft delete) del tipo de documento"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Tipo de documento eliminado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Tipo de documento no encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @Parameter(description = "ID del tipo de documento", required = true)
            @PathVariable UUID id) {
        tipoDocumentoService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Tipo de documento eliminado exitosamente"));
    }

    @Operation(
            summary = "Obtener tipo de documento por nombre",
            description = "Busca un tipo de documento por su nombre (ej: DUI, PASAPORTE, NIT). " +
                    "Útil para obtener el UUID del tipo de documento a partir de su nombre legible."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Tipo de documento encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TipoDocumentoDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Tipo de documento no encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<TipoDocumentoDTO> findByNombre(
            @Parameter(description = "Nombre del tipo de documento (ej: DUI, PASAPORTE)", required = true)
            @PathVariable String nombre) {
        return ResponseEntity.ok(tipoDocumentoService.findByNombre(nombre));
    }

    @Operation(
            summary = "Activar tipo de documento",
            description = "Activa un tipo de documento previamente eliminado"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Tipo de documento activado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Tipo de documento no encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "El tipo de documento ya está activo o nombre duplicado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/{id}/activar")
    public ResponseEntity<ApiResponse<TipoDocumentoDTO>> activate(
            @Parameter(description = "ID del tipo de documento", required = true)
            @PathVariable UUID id) {
        TipoDocumentoDTO activated = tipoDocumentoService.activate(id);
        return ResponseEntity.ok(ApiResponse.success("Tipo de documento activado exitosamente", activated));
    }
}
