/**
 * Configuración del ambiente de producción
 * Sistema de Gestión de Trámites Gubernamentales
 * 
 * En Docker, Nginx actúa como proxy reverso
 * Las peticiones a /api/ se redirigen al backend
 */
export const environment = {
  production: true,
  apiUrl: '/api/v1'
};
