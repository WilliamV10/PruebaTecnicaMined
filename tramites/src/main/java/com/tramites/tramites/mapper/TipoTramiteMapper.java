package com.tramites.tramites.mapper;

import com.tramites.tramites.dto.TipoTramiteCreateDTO;
import com.tramites.tramites.dto.TipoTramiteDTO;
import com.tramites.tramites.entity.TipoTramite;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

/**
 * Mapper para conversión entre TipoTramite Entity y DTOs.
 */
@Mapper(componentModel = "spring")
public interface TipoTramiteMapper {

    TipoTramiteDTO toDTO(TipoTramite entity);

    List<TipoTramiteDTO> toDTOList(List<TipoTramite> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "fechaModificacion", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    TipoTramite toEntity(TipoTramiteCreateDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "fechaModificacion", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    void updateEntityFromDTO(TipoTramiteCreateDTO dto, @MappingTarget TipoTramite entity);
}
