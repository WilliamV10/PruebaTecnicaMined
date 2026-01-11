package com.tramites.tramites.controller;

import com.tramites.tramites.dto.ApiResponse;
import com.tramites.tramites.dto.TramiteCreateDTO;
import com.tramites.tramites.dto.TramiteDTO;
import com.tramites.tramites.dto.TramiteEstadoDTO;
import com.tramites.tramites.exception.ErrorResponse;
import com.tramites.tramites.service.TramiteService;
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
 * Controlador REST para gestión de trámites ciudadanos.
 * Permite la solicitud y seguimiento de trámites gubernamentales.
 */
@RestController
@RequestMapping("/api/v1/tramites")
@RequiredArgsConstructor
@Tag(name = "Trámites", description = "API para gestión de trámites ciudadanos ante la institución gubernamental")
public class TramiteController {

    private final TramiteService tramiteService;

    @Operation(
            summary = "Listar trámites",
            description = "Obtiene todos los trámites activos registrados en el sistema"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TramiteDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<TramiteDTO>> findAll() {
        return ResponseEntity.ok(tramiteService.findAll());
    }

    @Operation(
            summary = "Obtener trámite por ID",
            description = "Busca un trámite específico por su identificador único"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Trámite encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TramiteDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Trámite no encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<TramiteDTO> findById(
            @Parameter(description = "ID del trámite", required = true)
            @PathVariable UUID id) {
        return ResponseEntity.ok(tramiteService.findById(id));
    }

    @Operation(
            summary = "Listar trámites por ciudadano",
            description = "Obtiene todos los trámites activos de un ciudadano específico"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TramiteDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Ciudadano no encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/ciudadano/{ciudadanoId}")
    public ResponseEntity<List<TramiteDTO>> findByCiudadanoId(
            @Parameter(description = "ID del ciudadano", required = true)
            @PathVariable UUID ciudadanoId) {
        return ResponseEntity.ok(tramiteService.findByCiudadanoId(ciudadanoId));
    }

    @Operation(
            summary = "Registrar trámite",
            description = "Registra una nueva solicitud de trámite para un ciudadano. El estado inicial siempre será PENDIENTE."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Trámite registrado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos inválidos",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Ciudadano o tipo de trámite no encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<ApiResponse<TramiteDTO>> create(
            @Parameter(description = "Datos del trámite", required = true)
            @Valid @RequestBody TramiteCreateDTO dto) {
        TramiteDTO created = tramiteService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Trámite registrado exitosamente", created));
    }

    @Operation(
            summary = "Actualizar estado del trámite",
            description = "Actualiza el estado de un trámite existente. Estados válidos: PENDIENTE, APROBADO, RECHAZADO"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Estado actualizado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Estado inválido",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Trámite no encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/{id}/estado")
    public ResponseEntity<ApiResponse<TramiteDTO>> updateEstado(
            @Parameter(description = "ID del trámite", required = true)
            @PathVariable UUID id,
            @Valid @RequestBody TramiteEstadoDTO estadoDTO) {
        TramiteDTO updated = tramiteService.updateEstado(id, estadoDTO);
        return ResponseEntity.ok(ApiResponse.success("Estado del trámite actualizado exitosamente", updated));
    }

    @Operation(
            summary = "Aprobar trámite",
            description = "Aprueba un trámite que esté en estado PENDIENTE"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Trámite aprobado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "El trámite no está en estado PENDIENTE",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Trámite no encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/{id}/aprobar")
    public ResponseEntity<ApiResponse<TramiteDTO>> aprobar(
            @Parameter(description = "ID del trámite", required = true)
            @PathVariable UUID id,
            @Parameter(description = "Observación opcional sobre la aprobación")
            @RequestParam(required = false) String observacion) {
        TramiteDTO aprobado = tramiteService.aprobar(id, observacion);
        return ResponseEntity.ok(ApiResponse.success("Trámite aprobado exitosamente", aprobado));
    }

    @Operation(
            summary = "Rechazar trámite",
            description = "Rechaza un trámite que esté en estado PENDIENTE"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Trámite rechazado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "El trámite no está en estado PENDIENTE",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Trámite no encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/{id}/rechazar")
    public ResponseEntity<ApiResponse<TramiteDTO>> rechazar(
            @Parameter(description = "ID del trámite", required = true)
            @PathVariable UUID id,
            @Parameter(description = "Motivo del rechazo")
            @RequestParam(required = false) String observacion) {
        TramiteDTO rechazado = tramiteService.rechazar(id, observacion);
        return ResponseEntity.ok(ApiResponse.success("Trámite rechazado exitosamente", rechazado));
    }

    @Operation(
            summary = "Eliminar trámite",
            description = "Realiza una eliminación lógica (soft delete) del trámite"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Trámite eliminado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Trámite no encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @Parameter(description = "ID del trámite", required = true)
            @PathVariable UUID id) {
        tramiteService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Trámite eliminado exitosamente"));
    }

    @Operation(
            summary = "Activar trámite",
            description = "Activa un trámite previamente eliminado"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Trámite activado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Trámite no encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "El trámite ya está activo",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/{id}/activar")
    public ResponseEntity<ApiResponse<TramiteDTO>> activate(
            @Parameter(description = "ID del trámite", required = true)
            @PathVariable UUID id) {
        TramiteDTO activated = tramiteService.activate(id);
        return ResponseEntity.ok(ApiResponse.success("Trámite activado exitosamente", activated));
    }

    @Operation(
            summary = "Contar trámites pendientes",
            description = "Obtiene el número total de trámites activos en estado PENDIENTE"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Conteo obtenido exitosamente")
    })
    @GetMapping("/count/pendientes")
    public ResponseEntity<Long> countPendientes() {
        return ResponseEntity.ok(tramiteService.countPendientes());
    }
}
