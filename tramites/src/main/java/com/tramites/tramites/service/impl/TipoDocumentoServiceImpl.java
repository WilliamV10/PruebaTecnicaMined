package com.tramites.tramites.service.impl;

import com.tramites.tramites.dto.TipoDocumentoCreateDTO;
import com.tramites.tramites.dto.TipoDocumentoDTO;
import com.tramites.tramites.entity.TipoDocumento;
import com.tramites.tramites.exception.BusinessException;
import com.tramites.tramites.exception.ResourceNotFoundException;
import com.tramites.tramites.mapper.TipoDocumentoMapper;
import com.tramites.tramites.repository.CiudadanoRepository;
import com.tramites.tramites.repository.TipoDocumentoRepository;
import com.tramites.tramites.service.TipoDocumentoService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Implementación del servicio de TipoDocumento.
 * Contiene la lógica de negocio para gestionar tipos de documento.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class TipoDocumentoServiceImpl implements TipoDocumentoService {

    private static final Logger logger = LoggerFactory.getLogger(TipoDocumentoServiceImpl.class);

    private final TipoDocumentoRepository tipoDocumentoRepository;
    private final TipoDocumentoMapper tipoDocumentoMapper;
    private final CiudadanoRepository ciudadanoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<TipoDocumentoDTO> findAll() {
        logger.info("Consultando todos los tipos de documento activos");
        List<TipoDocumento> tiposDocumento = tipoDocumentoRepository.findAllByIsActiveTrue();
        return tipoDocumentoMapper.toDTOList(tiposDocumento);
    }

    @Override
    @Transactional(readOnly = true)
    public TipoDocumentoDTO findById(UUID id) {
        logger.info("Buscando tipo de documento con ID: {}", id);
        TipoDocumento tipoDocumento = tipoDocumentoRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de Documento", "id", id));
        return tipoDocumentoMapper.toDTO(tipoDocumento);
    }

    @Override
    @Transactional(readOnly = true)
    public TipoDocumentoDTO findByNombre(String nombre) {
        logger.info("Buscando tipo de documento con nombre: {}", nombre);
        TipoDocumento tipoDocumento = tipoDocumentoRepository.findByNombreIgnoreCaseAndIsActiveTrue(nombre)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de Documento", "nombre", nombre));
        return tipoDocumentoMapper.toDTO(tipoDocumento);
    }

    @Override
    public TipoDocumentoDTO create(TipoDocumentoCreateDTO dto) {
        logger.info("Creando nuevo tipo de documento: {}", dto.getNombre());
        
        // Validar que no exista un tipo de documento con el mismo nombre
        if (tipoDocumentoRepository.existsByNombreIgnoreCaseAndIsActiveTrue(dto.getNombre())) {
            throw new BusinessException("Ya existe un tipo de documento con el nombre: " + dto.getNombre());
        }

        TipoDocumento tipoDocumento = tipoDocumentoMapper.toEntity(dto);
        TipoDocumento saved = tipoDocumentoRepository.save(tipoDocumento);
        
        logger.info("Tipo de documento creado exitosamente con ID: {}", saved.getId());
        return tipoDocumentoMapper.toDTO(saved);
    }

    @Override
    public TipoDocumentoDTO update(UUID id, TipoDocumentoCreateDTO dto) {
        logger.info("Actualizando tipo de documento con ID: {}", id);
        
        TipoDocumento tipoDocumento = tipoDocumentoRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de Documento", "id", id));

        // Validar nombre único si cambió
        if (!tipoDocumento.getNombre().equalsIgnoreCase(dto.getNombre()) 
                && tipoDocumentoRepository.existsByNombreIgnoreCaseAndIsActiveTrue(dto.getNombre())) {
            throw new BusinessException("Ya existe un tipo de documento con el nombre: " + dto.getNombre());
        }

        tipoDocumentoMapper.updateEntityFromDTO(dto, tipoDocumento);
        TipoDocumento updated = tipoDocumentoRepository.save(tipoDocumento);
        
        logger.info("Tipo de documento actualizado exitosamente");
        return tipoDocumentoMapper.toDTO(updated);
    }

    @Override
    public void delete(UUID id) {
        logger.info("Eliminando (soft delete) tipo de documento con ID: {}", id);
        
        TipoDocumento tipoDocumento = tipoDocumentoRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de Documento", "id", id));

        // Validar que no existan ciudadanos activos usando este tipo de documento
        if (ciudadanoRepository.existsByTipoDocumentoIdAndIsActiveTrue(id)) {
            throw new BusinessException("No se puede eliminar el tipo de documento porque tiene ciudadanos asociados");
        }

        tipoDocumento.setIsActive(false);
        tipoDocumentoRepository.save(tipoDocumento);
        
        logger.info("Tipo de documento eliminado exitosamente (soft delete)");
    }

    @Override
    public TipoDocumentoDTO activate(UUID id) {
        logger.info("Activando tipo de documento con ID: {}", id);
        
        // Usar consulta nativa para ignorar @SQLRestriction y encontrar registros inactivos
        TipoDocumento tipoDocumento = tipoDocumentoRepository.findByIdIncludingInactive(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de Documento", "id", id));
        
        if (tipoDocumento.getIsActive()) {
            throw new BusinessException("El tipo de documento ya está activo");
        }

        // Validar que no exista otro con el mismo nombre activo
        if (tipoDocumentoRepository.existsByNombreIgnoreCaseAndIsActiveTrue(tipoDocumento.getNombre())) {
            throw new BusinessException("Ya existe un tipo de documento activo con el mismo nombre");
        }

        tipoDocumento.setIsActive(true);
        TipoDocumento saved = tipoDocumentoRepository.save(tipoDocumento);
        
        logger.info("Tipo de documento activado exitosamente");
        return tipoDocumentoMapper.toDTO(saved);
    }
}
