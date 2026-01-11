import { TipoDocumento } from './tipo-documento.model';

/**
 * Interface para Ciudadano
 * Representa a un ciudadano registrado en el sistema
 */
export interface Ciudadano {
  id: string;
  tipoDocumento: TipoDocumento;
  numeroDocumento: string;
  nombre: string;
  correo: string;
}

/**
 * DTO para crear un nuevo ciudadano
 * Puede usar tipoDocumentoId o tipoDocumentoNombre
 */
export interface CiudadanoCreate {
  tipoDocumentoId?: string;
  tipoDocumentoNombre?: string;
  numeroDocumento: string;
  nombre: string;
  correo: string;
}

/**
 * DTO para actualizar un ciudadano existente
 */
export interface CiudadanoUpdate {
  tipoDocumentoId?: string;
  tipoDocumentoNombre?: string;
  numeroDocumento: string;
  nombre: string;
  correo: string;
}
