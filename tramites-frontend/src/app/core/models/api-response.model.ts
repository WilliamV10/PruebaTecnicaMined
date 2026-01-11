/**
 * Modelo genérico para respuestas de la API
 * Coincide con ApiResponse<T> del backend
 */
export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
  timestamp: string;
}
