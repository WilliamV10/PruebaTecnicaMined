package com.tramites.tramites.repository;

import com.tramites.tramites.entity.Ciudadano;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio para operaciones de acceso a datos de Ciudadano.
 */
@Repository
public interface CiudadanoRepository extends JpaRepository<Ciudadano, UUID> {

    /**
     * Busca todos los ciudadanos activos.
     */
    List<Ciudadano> findAllByIsActiveTrue();

    /**
     * Busca un ciudadano activo por su ID.
     */
    Optional<Ciudadano> findByIdAndIsActiveTrue(UUID id);

    /**
     * Verifica si existe un ciudadano con el tipo y número de documento dados.
     */
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END " +
           "FROM Ciudadano c WHERE c.tipoDocumento.id = :tipoDocumentoId " +
           "AND c.numeroDocumento = :numeroDocumento AND c.isActive = true")
    boolean existsByTipoDocumentoIdAndNumeroDocumento(
            @Param("tipoDocumentoId") UUID tipoDocumentoId,
            @Param("numeroDocumento") String numeroDocumento);

    /**
     * Verifica si existe un ciudadano con el tipo y número de documento dados, excluyendo un ID específico.
     */
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END " +
           "FROM Ciudadano c WHERE c.tipoDocumento.id = :tipoDocumentoId " +
           "AND c.numeroDocumento = :numeroDocumento AND c.id <> :excludeId AND c.isActive = true")
    boolean existsByTipoDocumentoIdAndNumeroDocumentoAndIdNot(
            @Param("tipoDocumentoId") UUID tipoDocumentoId,
            @Param("numeroDocumento") String numeroDocumento,
            @Param("excludeId") UUID excludeId);

    /**
     * Verifica si existe algún ciudadano activo que use el tipo de documento especificado.
     */
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Ciudadano c WHERE c.tipoDocumento.id = :tipoDocumentoId AND c.isActive = true")
    boolean existsByTipoDocumentoIdAndIsActiveTrue(@Param("tipoDocumentoId") UUID tipoDocumentoId);

    /**
     * Verifica si existe un ciudadano activo con el correo dado.
     */
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Ciudadano c WHERE c.correo = :correo AND c.isActive = true")
    boolean existsByCorreoAndIsActiveTrue(@Param("correo") String correo);

    /**
     * Verifica si existe un ciudadano activo con el correo dado, excluyendo un ID específico.
     */
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Ciudadano c WHERE c.correo = :correo AND c.id <> :excludeId AND c.isActive = true")
    boolean existsByCorreoAndIdNotAndIsActiveTrue(@Param("correo") String correo, @Param("excludeId") UUID excludeId);

    /**
     * Busca un ciudadano por ID incluyendo los inactivos.
     * Usa consulta nativa para ignorar el @SQLRestriction de la entidad.
     * Necesario para el método de activación.
     */
    @Query(value = "SELECT * FROM ciudadano WHERE id = :id", nativeQuery = true)
    Optional<Ciudadano> findByIdIncludingInactive(@Param("id") UUID id);
}
