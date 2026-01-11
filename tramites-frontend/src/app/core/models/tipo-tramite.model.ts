/**
 * Interface para Tipo de Trámite
 * Representa los diferentes tipos de trámites disponibles
 */
export interface TipoTramite {
  id: string;
  nombre: string;
}

/**
 * DTO para crear un nuevo tipo de trámite
 */
export interface TipoTramiteCreate {
  nombre: string;
}
