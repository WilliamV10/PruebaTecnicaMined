import { Ciudadano } from './ciudadano.model';
import { TipoTramite } from './tipo-tramite.model';

/**
 * Estados posibles de un trámite
 */
export type EstadoTramite = 'PENDIENTE' | 'APROBADO' | 'RECHAZADO';

/**
 * Interface para Trámite
 * Representa un trámite realizado por un ciudadano
 */
export interface Tramite {
  id: string;
  ciudadano: Ciudadano;
  tipoTramite: TipoTramite;
  estado: EstadoTramite;
  observacion: string | null;
  fechaSolicitud: string;
}

/**
 * DTO para crear un nuevo trámite
 * Puede usar tipoTramiteId o tipoTramiteNombre
 */
export interface TramiteCreate {
  ciudadanoId: string;
  tipoTramiteId?: string;
  tipoTramiteNombre?: string;
  observacion?: string;
}

/**
 * DTO para actualizar el estado de un trámite
 */
export interface TramiteEstadoUpdate {
  estado: EstadoTramite;
  observacion?: string;
}
