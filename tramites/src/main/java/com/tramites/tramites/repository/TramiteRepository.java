package com.tramites.tramites.repository;

import com.tramites.tramites.entity.Tramite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio para operaciones de acceso a datos de Tramite.
 */
@Repository
public interface TramiteRepository extends JpaRepository<Tramite, UUID> {

    /**
     * Busca todos los trámites activos.
     */
    List<Tramite> findAllByIsActiveTrue();

    /**
     * Busca un trámite activo por su ID.
     */
    Optional<Tramite> findByIdAndIsActiveTrue(UUID id);

    /**
     * Busca todos los trámites activos de un ciudadano específico.
     */
    @Query("SELECT t FROM Tramite t WHERE t.ciudadano.id = :ciudadanoId AND t.isActive = true")
    List<Tramite> findAllByCiudadanoIdAndIsActiveTrue(@Param("ciudadanoId") UUID ciudadanoId);

    /**
     * Busca trámites por estado.
     */
    List<Tramite> findAllByEstadoAndIsActiveTrue(String estado);

    /**
     * Busca trámites por tipo de trámite.
     */
    @Query("SELECT t FROM Tramite t WHERE t.tipoTramite.id = :tipoTramiteId AND t.isActive = true")
    List<Tramite> findAllByTipoTramiteIdAndIsActiveTrue(@Param("tipoTramiteId") UUID tipoTramiteId);

    /**
     * Verifica si existe algún trámite activo que use el tipo de trámite especificado.
     */
    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM Tramite t WHERE t.tipoTramite.id = :tipoTramiteId AND t.isActive = true")
    boolean existsByTipoTramiteIdAndIsActiveTrue(@Param("tipoTramiteId") UUID tipoTramiteId);

    /**
     * Verifica si existe algún trámite activo para un ciudadano específico.
     */
    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM Tramite t WHERE t.ciudadano.id = :ciudadanoId AND t.isActive = true")
    boolean existsByCiudadanoIdAndIsActiveTrue(@Param("ciudadanoId") UUID ciudadanoId);

    /**
     * Cuenta los trámites activos con estado PENDIENTE.
     */
    @Query("SELECT COUNT(t) FROM Tramite t WHERE t.estado = 'PENDIENTE' AND t.isActive = true")
    long countByEstadoPendienteAndIsActiveTrue();

    /**
     * Verifica si existe un trámite PENDIENTE para un ciudadano con un tipo de trámite específico.
     * Esto evita que un ciudadano tenga múltiples trámites del mismo tipo en estado pendiente.
     */
    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM Tramite t " +
           "WHERE t.ciudadano.id = :ciudadanoId " +
           "AND t.tipoTramite.id = :tipoTramiteId " +
           "AND t.estado = 'PENDIENTE' " +
           "AND t.isActive = true")
    boolean existsByCiudadanoIdAndTipoTramiteIdAndEstadoPendienteAndIsActiveTrue(
            @Param("ciudadanoId") UUID ciudadanoId,
            @Param("tipoTramiteId") UUID tipoTramiteId);

    /**
     * Busca un trámite por ID incluyendo los inactivos.
     * Usa consulta nativa para ignorar el @SQLRestriction de la entidad.
     * Necesario para el método de activación.
     */
    @Query(value = "SELECT * FROM tramite WHERE id = :id", nativeQuery = true)
    Optional<Tramite> findByIdIncludingInactive(@Param("id") UUID id);
}
