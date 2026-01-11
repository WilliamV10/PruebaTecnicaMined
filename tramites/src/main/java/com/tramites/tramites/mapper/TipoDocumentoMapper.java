package com.tramites.tramites.mapper;

import com.tramites.tramites.dto.TipoDocumentoCreateDTO;
import com.tramites.tramites.dto.TipoDocumentoDTO;
import com.tramites.tramites.entity.TipoDocumento;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

/**
 * Mapper para conversión entre TipoDocumento Entity y DTOs.
 */
@Mapper(componentModel = "spring")
public interface TipoDocumentoMapper {

    TipoDocumentoDTO toDTO(TipoDocumento entity);

    List<TipoDocumentoDTO> toDTOList(List<TipoDocumento> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "fechaModificacion", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    TipoDocumento toEntity(TipoDocumentoCreateDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "fechaModificacion", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    void updateEntityFromDTO(TipoDocumentoCreateDTO dto, @MappingTarget TipoDocumento entity);
}
