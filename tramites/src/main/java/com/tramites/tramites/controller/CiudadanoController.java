package com.tramites.tramites.controller;

import com.tramites.tramites.dto.ApiResponse;
import com.tramites.tramites.dto.CiudadanoCreateDTO;
import com.tramites.tramites.dto.CiudadanoDTO;
import com.tramites.tramites.dto.CiudadanoUpdateDTO;
import com.tramites.tramites.exception.ErrorResponse;
import com.tramites.tramites.service.CiudadanoService;
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
 * Controlador REST para gestión de ciudadanos.
 * Permite el registro y administración de ciudadanos en el sistema gubernamental.
 */
@RestController
@RequestMapping("/api/v1/ciudadanos")
@RequiredArgsConstructor
@Tag(name = "Ciudadanos", description = "API para gestión de ciudadanos registrados en el sistema gubernamental")
public class CiudadanoController {

    private final CiudadanoService ciudadanoService;

    @Operation(
            summary = "Listar ciudadanos",
            description = "Obtiene todos los ciudadanos activos registrados en el sistema"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CiudadanoDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<CiudadanoDTO>> findAll() {
        return ResponseEntity.ok(ciudadanoService.findAll());
    }

    @Operation(
            summary = "Obtener ciudadano por ID",
            description = "Busca un ciudadano específico por su identificador único"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Ciudadano encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CiudadanoDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Ciudadano no encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<CiudadanoDTO> findById(
            @Parameter(description = "ID del ciudadano", required = true)
            @PathVariable UUID id) {
        return ResponseEntity.ok(ciudadanoService.findById(id));
    }

    @Operation(
            summary = "Registrar ciudadano",
            description = "Registra un nuevo ciudadano en el sistema gubernamental"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Ciudadano registrado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos inválidos o ciudadano ya registrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Tipo de documento no encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<ApiResponse<CiudadanoDTO>> create(
            @Parameter(description = "Datos del ciudadano", required = true)
            @Valid @RequestBody CiudadanoCreateDTO dto) {
        CiudadanoDTO created = ciudadanoService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Ciudadano registrado exitosamente", created));
    }

    @Operation(
            summary = "Actualizar ciudadano",
            description = "Actualiza los datos de un ciudadano existente"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Ciudadano actualizado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Ciudadano o tipo de documento no encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos inválidos o documento duplicado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CiudadanoDTO>> update(
            @Parameter(description = "ID del ciudadano", required = true)
            @PathVariable UUID id,
            @Parameter(description = "Datos actualizados", required = true)
            @Valid @RequestBody CiudadanoUpdateDTO dto) {
        CiudadanoDTO updated = ciudadanoService.update(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Ciudadano actualizado exitosamente", updated));
    }

    @Operation(
            summary = "Eliminar ciudadano",
            description = "Realiza una eliminación lógica (soft delete) del ciudadano"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Ciudadano eliminado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Ciudadano no encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @Parameter(description = "ID del ciudadano", required = true)
            @PathVariable UUID id) {
        ciudadanoService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Ciudadano eliminado exitosamente"));
    }

    @Operation(
            summary = "Activar ciudadano",
            description = "Activa un ciudadano previamente eliminado"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Ciudadano activado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Ciudadano no encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "El ciudadano ya está activo o existe conflicto de datos",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/{id}/activar")
    public ResponseEntity<ApiResponse<CiudadanoDTO>> activate(
            @Parameter(description = "ID del ciudadano", required = true)
            @PathVariable UUID id) {
        CiudadanoDTO activated = ciudadanoService.activate(id);
        return ResponseEntity.ok(ApiResponse.success("Ciudadano activado exitosamente", activated));
    }
}
