/**
 * Interface para Tipo de Documento
 * Representa los diferentes tipos de documentos de identificación
 */
export interface TipoDocumento {
  id: string;
  nombre: string;
}

/**
 * DTO para crear un nuevo tipo de documento
 */
export interface TipoDocumentoCreate {
  nombre: string;
}

/**
 * Configuración de validación para tipos de documento conocidos
 * Basado en el nombre del tipo de documento
 */
export interface DocumentoConfig {
  mascara: string;
  patronRegex: RegExp;
  placeholder: string;
  ejemplo: string;
  longitud: number;
}

/**
 * Mapa de configuraciones por nombre de tipo de documento
 */
export const DOCUMENTO_CONFIGS: Record<string, DocumentoConfig> = {
  'DUI': {
    mascara: '99999999-9',
    patronRegex: /^[0-9]{8}-[0-9]$/,
    placeholder: '________-_',
    ejemplo: '12345678-9',
    longitud: 9
  },
  'Pasaporte': {
    mascara: 'a-99999999',
    patronRegex: /^[A-Za-z]-[0-9]{8}$/,
    placeholder: '_-________',
    ejemplo: 'A-12345678',
    longitud: 9
  },
  'NIT': {
    mascara: '9999-999999-999-9',
    patronRegex: /^[0-9]{4}-[0-9]{6}-[0-9]{3}-[0-9]$/,
    placeholder: '____-______-___-_',
    ejemplo: '0614-150895-101-0',
    longitud: 14
  }
};

/**
 * Obtiene la configuración de un tipo de documento por nombre
 */
export function getDocumentoConfig(nombre: string): DocumentoConfig | null {
  return DOCUMENTO_CONFIGS[nombre] || null;
}
