/**
 * Interface para errores de validación
 */
export interface ValidationError {
  field: string;
  message: string;
}

/**
 * Interface para respuestas de error de la API
 */
export interface ApiError {
  timestamp: string;
  status: number;
  error: string;
  message: string;
  path: string;
  validationErrors?: ValidationError[];
}
