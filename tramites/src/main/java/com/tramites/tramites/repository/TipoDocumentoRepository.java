package com.tramites.tramites.repository;

import com.tramites.tramites.entity.TipoDocumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio para operaciones de acceso a datos de TipoDocumento.
 */
@Repository
public interface TipoDocumentoRepository extends JpaRepository<TipoDocumento, UUID> {

    /**
     * Busca todos los tipos de documento activos.
     */
    List<TipoDocumento> findAllByIsActiveTrue();

    /**
     * Busca un tipo de documento activo por su ID.
     */
    Optional<TipoDocumento> findByIdAndIsActiveTrue(UUID id);

    /**
     * Verifica si existe un tipo de documento activo con el nombre dado.
     */
    boolean existsByNombreIgnoreCaseAndIsActiveTrue(String nombre);

    /**
     * Busca un tipo de documento activo por nombre.
     */
    Optional<TipoDocumento> findByNombreIgnoreCaseAndIsActiveTrue(String nombre);

    /**
     * Busca un tipo de documento por ID incluyendo los inactivos.
     * Usa consulta nativa para ignorar el @SQLRestriction de la entidad.
     * Necesario para el método de activación.
     */
    @Query(value = "SELECT * FROM tipo_documento WHERE id = :id", nativeQuery = true)
    Optional<TipoDocumento> findByIdIncludingInactive(@Param("id") UUID id);
}
