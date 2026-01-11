package com.tramites.tramites.service;

import com.tramites.tramites.dto.CiudadanoCreateDTO;
import com.tramites.tramites.dto.CiudadanoDTO;
import com.tramites.tramites.dto.CiudadanoUpdateDTO;

import java.util.List;
import java.util.UUID;

/**
 * Contrato de servicio para operaciones de Ciudadano.
 */
public interface CiudadanoService {

    /**
     * Obtiene todos los ciudadanos activos.
     * @return Lista de ciudadanos
     */
    List<CiudadanoDTO> findAll();

    /**
     * Busca un ciudadano por su ID.
     * @param id Identificador único
     * @return Ciudadano encontrado
     */
    CiudadanoDTO findById(UUID id);

    /**
     * Registra un nuevo ciudadano.
     * @param dto Datos del ciudadano
     * @return Ciudadano registrado
     */
    CiudadanoDTO create(CiudadanoCreateDTO dto);

    /**
     * Actualiza un ciudadano existente.
     * @param id Identificador del ciudadano
     * @param dto Datos actualizados
     * @return Ciudadano actualizado
     */
    CiudadanoDTO update(UUID id, CiudadanoUpdateDTO dto);

    /**
     * Elimina lógicamente un ciudadano (soft delete).
     * @param id Identificador del ciudadano
     */
    void delete(UUID id);

    /**
     * Activa un ciudadano previamente eliminado.
     * @param id Identificador del ciudadano
     * @return Ciudadano activado
     */
    CiudadanoDTO activate(UUID id);
}
