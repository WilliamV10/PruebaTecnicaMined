package com.tramites.tramites.mapper;

import com.tramites.tramites.dto.TramiteCreateDTO;
import com.tramites.tramites.dto.TramiteDTO;
import com.tramites.tramites.entity.Ciudadano;
import com.tramites.tramites.entity.TipoTramite;
import com.tramites.tramites.entity.Tramite;
import org.mapstruct.*;

import java.util.List;
import java.util.UUID;

/**
 * Mapper para conversión entre Tramite Entity y DTOs.
 */
@Mapper(componentModel = "spring", uses = {CiudadanoMapper.class, TipoTramiteMapper.class})
public interface TramiteMapper {

    TramiteDTO toDTO(Tramite entity);

    List<TramiteDTO> toDTOList(List<Tramite> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaModificacion", ignore = true)
    @Mapping(target = "fechaSolicitud", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "ciudadano", ignore = true)
    @Mapping(target = "tipoTramite", ignore = true)
    @Mapping(target = "estado", ignore = true)
    Tramite toEntity(TramiteCreateDTO dto);

    default Ciudadano mapCiudadano(UUID ciudadanoId) {
        if (ciudadanoId == null) {
            return null;
        }
        Ciudadano ciudadano = new Ciudadano();
        ciudadano.setId(ciudadanoId);
        return ciudadano;
    }

    default TipoTramite mapTipoTramite(UUID tipoTramiteId) {
        if (tipoTramiteId == null) {
            return null;
        }
        TipoTramite tipoTramite = new TipoTramite();
        tipoTramite.setId(tipoTramiteId);
        return tipoTramite;
    }
}
