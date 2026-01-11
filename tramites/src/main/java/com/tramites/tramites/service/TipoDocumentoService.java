package com.tramites.tramites.service;

import com.tramites.tramites.dto.TipoDocumentoCreateDTO;
import com.tramites.tramites.dto.TipoDocumentoDTO;

import java.util.List;
import java.util.UUID;

/**
 * Contrato de servicio para operaciones de TipoDocumento.
 */
public interface TipoDocumentoService {

    /**
     * Obtiene todos los tipos de documento activos.
     * @return Lista de tipos de documento
     */
    List<TipoDocumentoDTO> findAll();

    /**
     * Busca un tipo de documento por su ID.
     * @param id Identificador único
     * @return Tipo de documento encontrado
     */
    TipoDocumentoDTO findById(UUID id);

    /**
     * Busca un tipo de documento por su nombre.
     * Útil para obtener el UUID cuando solo se conoce el nombre.
     * @param nombre Nombre del tipo de documento
     * @return Tipo de documento encontrado
     */
    TipoDocumentoDTO findByNombre(String nombre);

    /**
     * Crea un nuevo tipo de documento.
     * @param dto Datos del tipo de documento
     * @return Tipo de documento creado
     */
    TipoDocumentoDTO create(TipoDocumentoCreateDTO dto);

    /**
     * Actualiza un tipo de documento existente.
     * @param id Identificador del tipo de documento
     * @param dto Datos actualizados
     * @return Tipo de documento actualizado
     */
    TipoDocumentoDTO update(UUID id, TipoDocumentoCreateDTO dto);

    /**
     * Elimina lógicamente un tipo de documento (soft delete).
     * @param id Identificador del tipo de documento
     */
    void delete(UUID id);

    /**
     * Activa un tipo de documento previamente eliminado.
     * @param id Identificador del tipo de documento
     * @return Tipo de documento activado
     */
    TipoDocumentoDTO activate(UUID id);
}
