package com.tramites.tramites.mapper;

import com.tramites.tramites.dto.CiudadanoCreateDTO;
import com.tramites.tramites.dto.CiudadanoDTO;
import com.tramites.tramites.dto.CiudadanoUpdateDTO;
import com.tramites.tramites.entity.Ciudadano;
import com.tramites.tramites.entity.TipoDocumento;
import org.mapstruct.*;

import java.util.List;
import java.util.UUID;

/**
 * Mapper para conversión entre Ciudadano Entity y DTOs.
 */
@Mapper(componentModel = "spring", uses = {TipoDocumentoMapper.class})
public interface CiudadanoMapper {

    CiudadanoDTO toDTO(Ciudadano entity);

    List<CiudadanoDTO> toDTOList(List<Ciudadano> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "fechaModificacion", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "tipoDocumento", ignore = true)
    Ciudadano toEntity(CiudadanoCreateDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "fechaModificacion", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "tipoDocumento", ignore = true)
    void updateEntityFromDTO(CiudadanoUpdateDTO dto, @MappingTarget Ciudadano entity);

    default TipoDocumento mapTipoDocumento(UUID tipoDocumentoId) {
        if (tipoDocumentoId == null) {
            return null;
        }
        TipoDocumento tipoDocumento = new TipoDocumento();
        tipoDocumento.setId(tipoDocumentoId);
        return tipoDocumento;
    }
}
