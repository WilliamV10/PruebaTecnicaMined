package com.tramites.tramites.service.impl;

import com.tramites.tramites.dto.TipoTramiteCreateDTO;
import com.tramites.tramites.dto.TipoTramiteDTO;
import com.tramites.tramites.entity.TipoTramite;
import com.tramites.tramites.exception.BusinessException;
import com.tramites.tramites.exception.ResourceNotFoundException;
import com.tramites.tramites.mapper.TipoTramiteMapper;
import com.tramites.tramites.repository.TipoTramiteRepository;
import com.tramites.tramites.repository.TramiteRepository;
import com.tramites.tramites.service.TipoTramiteService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Implementación del servicio de TipoTramite.
 * Contiene la lógica de negocio para gestionar tipos de trámite.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class TipoTramiteServiceImpl implements TipoTramiteService {

    private static final Logger logger = LoggerFactory.getLogger(TipoTramiteServiceImpl.class);

    private final TipoTramiteRepository tipoTramiteRepository;
    private final TipoTramiteMapper tipoTramiteMapper;
    private final TramiteRepository tramiteRepository;

    @Override
    @Transactional(readOnly = true)
    public List<TipoTramiteDTO> findAll() {
        logger.info("Consultando todos los tipos de trámite activos");
        List<TipoTramite> tiposTramite = tipoTramiteRepository.findAllByIsActiveTrue();
        return tipoTramiteMapper.toDTOList(tiposTramite);
    }

    @Override
    @Transactional(readOnly = true)
    public TipoTramiteDTO findById(UUID id) {
        logger.info("Buscando tipo de trámite con ID: {}", id);
        TipoTramite tipoTramite = tipoTramiteRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de Trámite", "id", id));
        return tipoTramiteMapper.toDTO(tipoTramite);
    }

    @Override
    public TipoTramiteDTO create(TipoTramiteCreateDTO dto) {
        logger.info("Creando nuevo tipo de trámite: {}", dto.getNombre());
        
        // Validar que no exista un tipo de trámite con el mismo nombre
        if (tipoTramiteRepository.existsByNombreIgnoreCaseAndIsActiveTrue(dto.getNombre())) {
            throw new BusinessException("Ya existe un tipo de trámite con el nombre: " + dto.getNombre());
        }

        TipoTramite tipoTramite = tipoTramiteMapper.toEntity(dto);
        TipoTramite saved = tipoTramiteRepository.save(tipoTramite);
        
        logger.info("Tipo de trámite creado exitosamente con ID: {}", saved.getId());
        return tipoTramiteMapper.toDTO(saved);
    }

    @Override
    public TipoTramiteDTO update(UUID id, TipoTramiteCreateDTO dto) {
        logger.info("Actualizando tipo de trámite con ID: {}", id);
        
        TipoTramite tipoTramite = tipoTramiteRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de Trámite", "id", id));

        // Validar nombre único si cambió
        if (!tipoTramite.getNombre().equalsIgnoreCase(dto.getNombre()) 
                && tipoTramiteRepository.existsByNombreIgnoreCaseAndIsActiveTrue(dto.getNombre())) {
            throw new BusinessException("Ya existe un tipo de trámite con el nombre: " + dto.getNombre());
        }

        tipoTramiteMapper.updateEntityFromDTO(dto, tipoTramite);
        TipoTramite updated = tipoTramiteRepository.save(tipoTramite);
        
        logger.info("Tipo de trámite actualizado exitosamente");
        return tipoTramiteMapper.toDTO(updated);
    }

    @Override
    public void delete(UUID id) {
        logger.info("Eliminando (soft delete) tipo de trámite con ID: {}", id);
        
        TipoTramite tipoTramite = tipoTramiteRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de Trámite", "id", id));

        // Validar que no existan trámites activos usando este tipo de trámite
        if (tramiteRepository.existsByTipoTramiteIdAndIsActiveTrue(id)) {
            throw new BusinessException("No se puede eliminar el tipo de trámite porque tiene trámites asociados");
        }

        tipoTramite.setIsActive(false);
        tipoTramiteRepository.save(tipoTramite);
        
        logger.info("Tipo de trámite eliminado exitosamente (soft delete)");
    }

    @Override
    public TipoTramiteDTO activate(UUID id) {
        logger.info("Activando tipo de trámite con ID: {}", id);
        
        // Usar consulta nativa para ignorar @SQLRestriction y encontrar registros inactivos
        TipoTramite tipoTramite = tipoTramiteRepository.findByIdIncludingInactive(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de Trámite", "id", id));
        
        if (tipoTramite.getIsActive()) {
            throw new BusinessException("El tipo de trámite ya está activo");
        }

        // Validar que no exista otro con el mismo nombre activo
        if (tipoTramiteRepository.existsByNombreIgnoreCaseAndIsActiveTrue(tipoTramite.getNombre())) {
            throw new BusinessException("Ya existe un tipo de trámite activo con el mismo nombre");
        }

        tipoTramite.setIsActive(true);
        TipoTramite saved = tipoTramiteRepository.save(tipoTramite);
        
        logger.info("Tipo de trámite activado exitosamente");
        return tipoTramiteMapper.toDTO(saved);
    }
}
