package com.tramites.tramites.service;

import com.tramites.tramites.dto.TipoTramiteCreateDTO;
import com.tramites.tramites.dto.TipoTramiteDTO;

import java.util.List;
import java.util.UUID;

/**
 * Contrato de servicio para operaciones de TipoTramite.
 */
public interface TipoTramiteService {

    /**
     * Obtiene todos los tipos de trámite activos.
     * @return Lista de tipos de trámite
     */
    List<TipoTramiteDTO> findAll();

    /**
     * Busca un tipo de trámite por su ID.
     * @param id Identificador único
     * @return Tipo de trámite encontrado
     */
    TipoTramiteDTO findById(UUID id);

    /**
     * Crea un nuevo tipo de trámite.
     * @param dto Datos del tipo de trámite
     * @return Tipo de trámite creado
     */
    TipoTramiteDTO create(TipoTramiteCreateDTO dto);

    /**
     * Actualiza un tipo de trámite existente.
     * @param id Identificador del tipo de trámite
     * @param dto Datos actualizados
     * @return Tipo de trámite actualizado
     */
    TipoTramiteDTO update(UUID id, TipoTramiteCreateDTO dto);

    /**
     * Elimina lógicamente un tipo de trámite (soft delete).
     * @param id Identificador del tipo de trámite
     */
    void delete(UUID id);

    /**
     * Activa un tipo de trámite previamente eliminado.
     * @param id Identificador del tipo de trámite
     * @return Tipo de trámite activado
     */
    TipoTramiteDTO activate(UUID id);
}
