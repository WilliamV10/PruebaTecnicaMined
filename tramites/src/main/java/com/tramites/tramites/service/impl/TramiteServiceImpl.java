package com.tramites.tramites.service.impl;

import com.tramites.tramites.dto.TramiteCreateDTO;
import com.tramites.tramites.dto.TramiteDTO;
import com.tramites.tramites.dto.TramiteEstadoDTO;
import com.tramites.tramites.entity.Ciudadano;
import com.tramites.tramites.entity.TipoTramite;
import com.tramites.tramites.entity.Tramite;
import com.tramites.tramites.enums.EstadoTramite;
import com.tramites.tramites.exception.BusinessException;
import com.tramites.tramites.exception.ResourceNotFoundException;
import com.tramites.tramites.mapper.TramiteMapper;
import com.tramites.tramites.repository.CiudadanoRepository;
import com.tramites.tramites.repository.TipoTramiteRepository;
import com.tramites.tramites.repository.TramiteRepository;
import com.tramites.tramites.service.TramiteService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Implementación del servicio de Tramite.
 * Contiene la lógica de negocio para gestionar trámites ciudadanos.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class TramiteServiceImpl implements TramiteService {

    private static final Logger logger = LoggerFactory.getLogger(TramiteServiceImpl.class);

    private final TramiteRepository tramiteRepository;
    private final CiudadanoRepository ciudadanoRepository;
    private final TipoTramiteRepository tipoTramiteRepository;
    private final TramiteMapper tramiteMapper;

    @Override
    @Transactional(readOnly = true)
    public List<TramiteDTO> findAll() {
        logger.info("Consultando todos los trámites activos");
        List<Tramite> tramites = tramiteRepository.findAllByIsActiveTrue();
        return tramiteMapper.toDTOList(tramites);
    }

    @Override
    @Transactional(readOnly = true)
    public TramiteDTO findById(UUID id) {
        logger.info("Buscando trámite con ID: {}", id);
        Tramite tramite = tramiteRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trámite", "id", id));
        return tramiteMapper.toDTO(tramite);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TramiteDTO> findByCiudadanoId(UUID ciudadanoId) {
        logger.info("Consultando trámites del ciudadano con ID: {}", ciudadanoId);
        
        // Validar que exista el ciudadano
        if (!ciudadanoRepository.findByIdAndIsActiveTrue(ciudadanoId).isPresent()) {
            throw new ResourceNotFoundException("Ciudadano", "id", ciudadanoId);
        }
        
        List<Tramite> tramites = tramiteRepository.findAllByCiudadanoIdAndIsActiveTrue(ciudadanoId);
        return tramiteMapper.toDTOList(tramites);
    }

    @Override
    public TramiteDTO create(TramiteCreateDTO dto) {
        logger.info("Registrando nuevo trámite para ciudadano: {}", dto.getCiudadanoId());
        
        // Validar que exista el ciudadano
        Ciudadano ciudadano = ciudadanoRepository.findByIdAndIsActiveTrue(dto.getCiudadanoId())
                .orElseThrow(() -> new ResourceNotFoundException("Ciudadano", "id", dto.getCiudadanoId()));

        // Obtener tipo de trámite por ID o por nombre
        TipoTramite tipoTramite = resolverTipoTramite(dto.getTipoTramiteId(), dto.getTipoTramiteNombre());

        // Validar que el ciudadano no tenga un trámite PENDIENTE del mismo tipo
        if (tramiteRepository.existsByCiudadanoIdAndTipoTramiteIdAndEstadoPendienteAndIsActiveTrue(
                ciudadano.getId(), tipoTramite.getId())) {
            throw new BusinessException("El ciudadano ya tiene un trámite de tipo '" + tipoTramite.getNombre() + 
                    "' en estado PENDIENTE. Debe esperar a que sea aprobado o rechazado antes de solicitar otro.");
        }

        Tramite tramite = tramiteMapper.toEntity(dto);
        tramite.setCiudadano(ciudadano);
        tramite.setTipoTramite(tipoTramite);
        tramite.setEstado(EstadoTramite.PENDIENTE.getValor()); // Estado inicial siempre PENDIENTE
        
        Tramite saved = tramiteRepository.save(tramite);
        
        logger.info("Trámite registrado exitosamente con ID: {}", saved.getId());
        return tramiteMapper.toDTO(saved);
    }

    @Override
    public TramiteDTO updateEstado(UUID id, TramiteEstadoDTO estadoDTO) {
        logger.info("Actualizando estado del trámite {} a: {}", id, estadoDTO.getEstado());
        
        Tramite tramite = tramiteRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trámite", "id", id));

        // Validar que el estado sea válido
        if (!EstadoTramite.isValid(estadoDTO.getEstado())) {
            throw new BusinessException("Estado inválido: " + estadoDTO.getEstado() + 
                ". Los estados válidos son: PENDIENTE, APROBADO, RECHAZADO");
        }

        tramite.setEstado(estadoDTO.getEstado());
        
        // Actualizar observación si se proporciona
        if (estadoDTO.getObservacion() != null && !estadoDTO.getObservacion().isBlank()) {
            tramite.setObservacion(estadoDTO.getObservacion());
        }
        
        Tramite updated = tramiteRepository.save(tramite);
        
        logger.info("Estado del trámite actualizado exitosamente a: {}", estadoDTO.getEstado());
        return tramiteMapper.toDTO(updated);
    }

    @Override
    public TramiteDTO aprobar(UUID id, String observacion) {
        logger.info("Aprobando trámite con ID: {}", id);
        
        Tramite tramite = tramiteRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trámite", "id", id));

        // Validar que el trámite esté en estado PENDIENTE
        if (!EstadoTramite.PENDIENTE.getValor().equals(tramite.getEstado())) {
            throw new BusinessException("Solo se pueden aprobar trámites en estado PENDIENTE. " +
                "Estado actual: " + tramite.getEstado());
        }

        tramite.setEstado(EstadoTramite.APROBADO.getValor());
        
        if (observacion != null && !observacion.isBlank()) {
            tramite.setObservacion(observacion);
        }
        
        Tramite updated = tramiteRepository.save(tramite);
        
        logger.info("Trámite aprobado exitosamente");
        return tramiteMapper.toDTO(updated);
    }

    @Override
    public TramiteDTO rechazar(UUID id, String observacion) {
        logger.info("Rechazando trámite con ID: {}", id);
        
        Tramite tramite = tramiteRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trámite", "id", id));

        // Validar que el trámite esté en estado PENDIENTE
        if (!EstadoTramite.PENDIENTE.getValor().equals(tramite.getEstado())) {
            throw new BusinessException("Solo se pueden rechazar trámites en estado PENDIENTE. " +
                "Estado actual: " + tramite.getEstado());
        }

        tramite.setEstado(EstadoTramite.RECHAZADO.getValor());
        
        if (observacion != null && !observacion.isBlank()) {
            tramite.setObservacion(observacion);
        }
        
        Tramite updated = tramiteRepository.save(tramite);
        
        logger.info("Trámite rechazado exitosamente");
        return tramiteMapper.toDTO(updated);
    }

    @Override
    public void delete(UUID id) {
        logger.info("Eliminando (soft delete) trámite con ID: {}", id);
        
        Tramite tramite = tramiteRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trámite", "id", id));

        tramite.setIsActive(false);
        tramiteRepository.save(tramite);
        
        logger.info("Trámite eliminado exitosamente (soft delete)");
    }

    @Override
    public TramiteDTO activate(UUID id) {
        logger.info("Activando trámite con ID: {}", id);
        
        // Usar consulta nativa para ignorar @SQLRestriction y encontrar registros inactivos
        Tramite tramite = tramiteRepository.findByIdIncludingInactive(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trámite", "id", id));
        
        if (tramite.getIsActive()) {
            throw new BusinessException("El trámite ya está activo");
        }

        tramite.setIsActive(true);
        Tramite saved = tramiteRepository.save(tramite);
        
        logger.info("Trámite activado exitosamente");
        return tramiteMapper.toDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public long countPendientes() {
        logger.info("Contando trámites pendientes");
        return tramiteRepository.countByEstadoPendienteAndIsActiveTrue();
    }

    /**
     * Resuelve el tipo de trámite por ID o por nombre.
     * Prioriza el ID si ambos están presentes.
     */
    private TipoTramite resolverTipoTramite(UUID tipoTramiteId, String tipoTramiteNombre) {
        // Si se proporciona ID, buscar por ID
        if (tipoTramiteId != null) {
            return tipoTramiteRepository.findByIdAndIsActiveTrue(tipoTramiteId)
                    .orElseThrow(() -> new ResourceNotFoundException("Tipo de Trámite", "id", tipoTramiteId));
        }
        
        // Si se proporciona nombre, buscar por nombre
        if (tipoTramiteNombre != null && !tipoTramiteNombre.isBlank()) {
            return tipoTramiteRepository.findByNombreIgnoreCaseAndIsActiveTrue(tipoTramiteNombre)
                    .orElseThrow(() -> new ResourceNotFoundException("Tipo de Trámite", "nombre", tipoTramiteNombre));
        }
        
        // Si no se proporciona ninguno, lanzar error
        throw new BusinessException("Debe proporcionar tipoTramiteId o tipoTramiteNombre");
    }
}
