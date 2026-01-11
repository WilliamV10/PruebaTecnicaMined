package com.tramites.tramites.repository;

import com.tramites.tramites.entity.TipoTramite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio para operaciones de acceso a datos de TipoTramite.
 */
@Repository
public interface TipoTramiteRepository extends JpaRepository<TipoTramite, UUID> {

    /**
     * Busca todos los tipos de trámite activos.
     */
    List<TipoTramite> findAllByIsActiveTrue();

    /**
     * Busca un tipo de trámite activo por su ID.
     */
    Optional<TipoTramite> findByIdAndIsActiveTrue(UUID id);

    /**
     * Verifica si existe un tipo de trámite activo con el nombre dado.
     */
    boolean existsByNombreIgnoreCaseAndIsActiveTrue(String nombre);

    /**
     * Busca un tipo de trámite activo por nombre.
     */
    Optional<TipoTramite> findByNombreIgnoreCaseAndIsActiveTrue(String nombre);

    /**
     * Busca un tipo de trámite por ID incluyendo los inactivos.
     * Usa consulta nativa para ignorar el @SQLRestriction de la entidad.
     * Necesario para el método de activación.
     */
    @Query(value = "SELECT * FROM tipo_tramite WHERE id = :id", nativeQuery = true)
    Optional<TipoTramite> findByIdIncludingInactive(@Param("id") UUID id);
}
