package com.tramites.tramites.service;

import com.tramites.tramites.dto.TramiteCreateDTO;
import com.tramites.tramites.dto.TramiteDTO;
import com.tramites.tramites.dto.TramiteEstadoDTO;

import java.util.List;
import java.util.UUID;

/**
 * Contrato de servicio para operaciones de Tramite.
 */
public interface TramiteService {

    /**
     * Obtiene todos los trámites activos.
     * @return Lista de trámites
     */
    List<TramiteDTO> findAll();

    /**
     * Busca un trámite por su ID.
     * @param id Identificador único
     * @return Trámite encontrado
     */
    TramiteDTO findById(UUID id);

    /**
     * Busca todos los trámites de un ciudadano.
     * @param ciudadanoId Identificador del ciudadano
     * @return Lista de trámites del ciudadano
     */
    List<TramiteDTO> findByCiudadanoId(UUID ciudadanoId);

    /**
     * Registra un nuevo trámite.
     * @param dto Datos del trámite
     * @return Trámite registrado
     */
    TramiteDTO create(TramiteCreateDTO dto);

    /**
     * Actualiza el estado de un trámite.
     * @param id Identificador del trámite
     * @param estadoDTO Datos del nuevo estado y observación
     * @return Trámite actualizado
     */
    TramiteDTO updateEstado(UUID id, TramiteEstadoDTO estadoDTO);

    /**
     * Aprueba un trámite.
     * @param id Identificador del trámite
     * @param observacion Observación opcional
     * @return Trámite aprobado
     */
    TramiteDTO aprobar(UUID id, String observacion);

    /**
     * Rechaza un trámite.
     * @param id Identificador del trámite
     * @param observacion Motivo del rechazo
     * @return Trámite rechazado
     */
    TramiteDTO rechazar(UUID id, String observacion);

    /**
     * Elimina lógicamente un trámite (soft delete).
     * @param id Identificador del trámite
     */
    void delete(UUID id);

    /**
     * Activa un trámite previamente eliminado.
     * @param id Identificador del trámite
     * @return Trámite activado
     */
    TramiteDTO activate(UUID id);

    /**
     * Cuenta los trámites pendientes activos.
     * @return Número de trámites pendientes
     */
    long countPendientes();
}
