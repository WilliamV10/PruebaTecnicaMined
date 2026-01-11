# =====================================================
# Dockerfile para Frontend - Angular 17 + PrimeNG
# Sistema de Gestión de Trámites Gubernamentales
# =====================================================

# ----- ETAPA 1: Build -----
FROM node:20-alpine AS build

WORKDIR /app

# Copiar archivos de configuración de npm primero (para cache de dependencias)
COPY package*.json ./

# Instalar dependencias
RUN npm ci

# Copiar el resto del código fuente
COPY . .

# Construir la aplicación para producción
RUN npm run build -- --configuration=production

# ----- ETAPA 2: Nginx para servir la aplicación -----
FROM nginx:alpine

# Instalar curl para health checks
RUN apk add --no-cache curl

# Copiar configuración personalizada de Nginx
COPY nginx.conf /etc/nginx/nginx.conf

# Copiar los archivos compilados desde la etapa de build
COPY --from=build /app/dist/tramites-frontend/browser /usr/share/nginx/html

# Crear usuario no-root por seguridad
RUN chown -R nginx:nginx /usr/share/nginx/html && \
    chown -R nginx:nginx /var/cache/nginx && \
    chown -R nginx:nginx /var/log/nginx && \
    touch /var/run/nginx.pid && \
    chown -R nginx:nginx /var/run/nginx.pid

# Puerto de la aplicación
EXPOSE 80

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=10s --retries=3 \
    CMD curl -f http://localhost:80/ || exit 1

# Ejecutar Nginx
CMD ["nginx", "-g", "daemon off;"]
