package com.tramites.tramites.controller;

import com.tramites.tramites.dto.ApiResponse;
import com.tramites.tramites.dto.TipoTramiteCreateDTO;
import com.tramites.tramites.dto.TipoTramiteDTO;
import com.tramites.tramites.exception.ErrorResponse;
import com.tramites.tramites.service.TipoTramiteService;
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
 * Controlador REST para gestión de tipos de trámite gubernamental.
 * Catálogo de trámites disponibles en la institución pública.
 */
@RestController
@RequestMapping("/api/v1/tipos-tramite")
@RequiredArgsConstructor
@Tag(name = "Tipos de Trámite", description = "API para gestión del catálogo de tipos de trámite gubernamental")
public class TipoTramiteController {

    private final TipoTramiteService tipoTramiteService;

    @Operation(
            summary = "Listar tipos de trámite",
            description = "Obtiene todos los tipos de trámite activos disponibles en la institución"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TipoTramiteDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<TipoTramiteDTO>> findAll() {
        return ResponseEntity.ok(tipoTramiteService.findAll());
    }

    @Operation(
            summary = "Obtener tipo de trámite por ID",
            description = "Busca un tipo de trámite específico por su identificador único"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Tipo de trámite encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TipoTramiteDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Tipo de trámite no encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<TipoTramiteDTO> findById(
            @Parameter(description = "ID del tipo de trámite", required = true)
            @PathVariable UUID id) {
        return ResponseEntity.ok(tipoTramiteService.findById(id));
    }

    @Operation(
            summary = "Crear tipo de trámite",
            description = "Registra un nuevo tipo de trámite en el catálogo de la institución"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Tipo de trámite creado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos inválidos o código duplicado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<ApiResponse<TipoTramiteDTO>> create(
            @Parameter(description = "Datos del tipo de trámite", required = true)
            @Valid @RequestBody TipoTramiteCreateDTO dto) {
        TipoTramiteDTO created = tipoTramiteService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Tipo de trámite creado exitosamente", created));
    }

    @Operation(
            summary = "Actualizar tipo de trámite",
            description = "Actualiza los datos de un tipo de trámite existente"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Tipo de trámite actualizado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Tipo de trámite no encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos inválidos",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TipoTramiteDTO>> update(
            @Parameter(description = "ID del tipo de trámite", required = true)
            @PathVariable UUID id,
            @Parameter(description = "Datos actualizados", required = true)
            @Valid @RequestBody TipoTramiteCreateDTO dto) {
        TipoTramiteDTO updated = tipoTramiteService.update(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Tipo de trámite actualizado exitosamente", updated));
    }

    @Operation(
            summary = "Eliminar tipo de trámite",
            description = "Realiza una eliminación lógica (soft delete) del tipo de trámite"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Tipo de trámite eliminado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Tipo de trámite no encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @Parameter(description = "ID del tipo de trámite", required = true)
            @PathVariable UUID id) {
        tipoTramiteService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Tipo de trámite eliminado exitosamente"));
    }

    @Operation(
            summary = "Activar tipo de trámite",
            description = "Activa un tipo de trámite previamente eliminado"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Tipo de trámite activado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Tipo de trámite no encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "El tipo de trámite ya está activo o nombre duplicado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/{id}/activar")
    public ResponseEntity<ApiResponse<TipoTramiteDTO>> activate(
            @Parameter(description = "ID del tipo de trámite", required = true)
            @PathVariable UUID id) {
        TipoTramiteDTO activated = tipoTramiteService.activate(id);
        return ResponseEntity.ok(ApiResponse.success("Tipo de trámite activado exitosamente", activated));
    }
}
