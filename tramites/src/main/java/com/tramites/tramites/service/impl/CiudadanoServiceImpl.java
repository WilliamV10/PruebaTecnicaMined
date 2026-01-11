package com.tramites.tramites.service.impl;

import com.tramites.tramites.dto.CiudadanoCreateDTO;
import com.tramites.tramites.dto.CiudadanoDTO;
import com.tramites.tramites.dto.CiudadanoUpdateDTO;
import com.tramites.tramites.entity.Ciudadano;
import com.tramites.tramites.entity.TipoDocumento;
import com.tramites.tramites.exception.BusinessException;
import com.tramites.tramites.exception.ResourceNotFoundException;
import com.tramites.tramites.mapper.CiudadanoMapper;
import com.tramites.tramites.repository.CiudadanoRepository;
import com.tramites.tramites.repository.TipoDocumentoRepository;
import com.tramites.tramites.repository.TramiteRepository;
import com.tramites.tramites.service.CiudadanoService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Implementación del servicio de Ciudadano.
 * Contiene la lógica de negocio para gestionar ciudadanos.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CiudadanoServiceImpl implements CiudadanoService {

    private static final Logger logger = LoggerFactory.getLogger(CiudadanoServiceImpl.class);

    private final CiudadanoRepository ciudadanoRepository;
    private final TipoDocumentoRepository tipoDocumentoRepository;
    private final TramiteRepository tramiteRepository;
    private final CiudadanoMapper ciudadanoMapper;

    @Override
    @Transactional(readOnly = true)
    public List<CiudadanoDTO> findAll() {
        logger.info("Consultando todos los ciudadanos activos");
        List<Ciudadano> ciudadanos = ciudadanoRepository.findAllByIsActiveTrue();
        return ciudadanoMapper.toDTOList(ciudadanos);
    }

    @Override
    @Transactional(readOnly = true)
    public CiudadanoDTO findById(UUID id) {
        logger.info("Buscando ciudadano con ID: {}", id);
        Ciudadano ciudadano = ciudadanoRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ciudadano", "id", id));
        return ciudadanoMapper.toDTO(ciudadano);
    }

    @Override
    public CiudadanoDTO create(CiudadanoCreateDTO dto) {
        logger.info("Registrando nuevo ciudadano: {}", dto.getNombre());
        
        // Obtener tipo de documento por ID o por nombre
        TipoDocumento tipoDocumento = resolverTipoDocumento(dto.getTipoDocumentoId(), dto.getTipoDocumentoNombre());

        // Validar que no exista un ciudadano con el mismo documento
        if (ciudadanoRepository.existsByTipoDocumentoIdAndNumeroDocumento(
                tipoDocumento.getId(), dto.getNumeroDocumento())) {
            throw new BusinessException("Ya existe un ciudadano registrado con el documento: " 
                    + tipoDocumento.getNombre() + " - " + dto.getNumeroDocumento());
        }

        // Validar que el correo sea único
        if (ciudadanoRepository.existsByCorreoAndIsActiveTrue(dto.getCorreo())) {
            throw new BusinessException("Ya existe un ciudadano registrado con el correo: " + dto.getCorreo());
        }

        Ciudadano ciudadano = ciudadanoMapper.toEntity(dto);
        ciudadano.setTipoDocumento(tipoDocumento);
        
        Ciudadano saved = ciudadanoRepository.save(ciudadano);
        
        logger.info("Ciudadano registrado exitosamente con ID: {}", saved.getId());
        return ciudadanoMapper.toDTO(saved);
    }

    @Override
    public CiudadanoDTO update(UUID id, CiudadanoUpdateDTO dto) {
        logger.info("Actualizando ciudadano con ID: {}", id);
        
        Ciudadano ciudadano = ciudadanoRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ciudadano", "id", id));

        // Obtener tipo de documento por ID o por nombre
        TipoDocumento tipoDocumento = resolverTipoDocumento(dto.getTipoDocumentoId(), dto.getTipoDocumentoNombre());

        // Validar documento único si cambió
        boolean documentoChanged = !ciudadano.getTipoDocumento().getId().equals(tipoDocumento.getId())
                || !ciudadano.getNumeroDocumento().equals(dto.getNumeroDocumento());
        
        if (documentoChanged && ciudadanoRepository.existsByTipoDocumentoIdAndNumeroDocumentoAndIdNot(
                tipoDocumento.getId(), dto.getNumeroDocumento(), id)) {
            throw new BusinessException("Ya existe un ciudadano registrado con el documento: " 
                    + tipoDocumento.getNombre() + " - " + dto.getNumeroDocumento());
        }

        // Validar correo único si cambió
        boolean correoChanged = !dto.getCorreo().equalsIgnoreCase(ciudadano.getCorreo());
        
        if (correoChanged && ciudadanoRepository.existsByCorreoAndIdNotAndIsActiveTrue(dto.getCorreo(), id)) {
            throw new BusinessException("Ya existe un ciudadano registrado con el correo: " + dto.getCorreo());
        }

        ciudadanoMapper.updateEntityFromDTO(dto, ciudadano);
        ciudadano.setTipoDocumento(tipoDocumento);
        
        Ciudadano updated = ciudadanoRepository.save(ciudadano);
        
        logger.info("Ciudadano actualizado exitosamente");
        return ciudadanoMapper.toDTO(updated);
    }

    @Override
    public void delete(UUID id) {
        logger.info("Eliminando (soft delete) ciudadano con ID: {}", id);
        
        Ciudadano ciudadano = ciudadanoRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ciudadano", "id", id));

        // Validar que no tenga trámites activos
        if (tramiteRepository.existsByCiudadanoIdAndIsActiveTrue(id)) {
            throw new BusinessException("No se puede eliminar el ciudadano porque tiene trámites asociados");
        }

        ciudadano.setIsActive(false);
        ciudadanoRepository.save(ciudadano);
        
        logger.info("Ciudadano eliminado exitosamente (soft delete)");
    }

    @Override
    public CiudadanoDTO activate(UUID id) {
        logger.info("Activando ciudadano con ID: {}", id);
        
        // Usar consulta nativa para ignorar @SQLRestriction y encontrar registros inactivos
        Ciudadano ciudadano = ciudadanoRepository.findByIdIncludingInactive(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ciudadano", "id", id));
        
        if (ciudadano.getIsActive()) {
            throw new BusinessException("El ciudadano ya está activo");
        }

        // Validar que no exista otro ciudadano activo con el mismo documento
        if (ciudadanoRepository.existsByTipoDocumentoIdAndNumeroDocumento(
                ciudadano.getTipoDocumento().getId(), ciudadano.getNumeroDocumento())) {
            throw new BusinessException("Ya existe un ciudadano activo con el mismo documento");
        }

        // Validar que no exista otro ciudadano activo con el mismo correo
        if (ciudadanoRepository.existsByCorreoAndIsActiveTrue(ciudadano.getCorreo())) {
            throw new BusinessException("Ya existe un ciudadano activo con el mismo correo");
        }

        ciudadano.setIsActive(true);
        Ciudadano saved = ciudadanoRepository.save(ciudadano);
        
        logger.info("Ciudadano activado exitosamente");
        return ciudadanoMapper.toDTO(saved);
    }

    /**
     * Resuelve el tipo de documento por ID o por nombre.
     * Prioriza el ID si ambos están presentes.
     */
    private TipoDocumento resolverTipoDocumento(UUID tipoDocumentoId, String tipoDocumentoNombre) {
        // Si se proporciona ID, buscar por ID
        if (tipoDocumentoId != null) {
            return tipoDocumentoRepository.findByIdAndIsActiveTrue(tipoDocumentoId)
                    .orElseThrow(() -> new ResourceNotFoundException("Tipo de Documento", "id", tipoDocumentoId));
        }
        
        // Si se proporciona nombre, buscar por nombre
        if (tipoDocumentoNombre != null && !tipoDocumentoNombre.isBlank()) {
            return tipoDocumentoRepository.findByNombreIgnoreCaseAndIsActiveTrue(tipoDocumentoNombre)
                    .orElseThrow(() -> new ResourceNotFoundException("Tipo de Documento", "nombre", tipoDocumentoNombre));
        }
        
        // Si no se proporciona ninguno, lanzar error
        throw new BusinessException("Debe proporcionar tipoDocumentoId o tipoDocumentoNombre");
    }
}
