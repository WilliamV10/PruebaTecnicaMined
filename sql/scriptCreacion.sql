-- =====================================================
-- PRUEBA TÉCNICA – SISTEMA GUBERNAMENTAL
-- Base de datos: PostgreSQL
-- =====================================================

CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- =====================================================
-- CATÁLOGO: TIPO_DOCUMENTO
-- =====================================================
CREATE TABLE tipo_documento (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nombre VARCHAR(100) NOT NULL UNIQUE,
    ban_activo BOOLEAN NOT NULL DEFAULT TRUE
);

COMMENT ON TABLE tipo_documento IS 'Catálogo de tipos de documentos de identificación';
COMMENT ON COLUMN tipo_documento.ban_activo IS 'Indica si el registro está activo (soft delete)';

-- =====================================================
-- TABLA: CIUDADANO
-- =====================================================
CREATE TABLE ciudadano (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tipo_documento_id UUID NOT NULL,
    numero_documento VARCHAR(30) NOT NULL,
    nombre VARCHAR(150) NOT NULL,
    correo VARCHAR(150),
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ban_activo BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT fk_ciudadano_tipo_documento
        FOREIGN KEY (tipo_documento_id)
        REFERENCES tipo_documento(id),

    CONSTRAINT uk_ciudadano_documento
        UNIQUE (tipo_documento_id, numero_documento)
);

COMMENT ON TABLE ciudadano IS 'Registro de ciudadanos';
COMMENT ON COLUMN ciudadano.ban_activo IS 'Soft delete lógico';

-- =====================================================
-- CATÁLOGO: TIPO_TRAMITE
-- =====================================================
CREATE TABLE tipo_tramite (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    codigo VARCHAR(20) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    ban_activo BOOLEAN NOT NULL DEFAULT TRUE
);

COMMENT ON TABLE tipo_tramite IS 'Catálogo de tipos de trámite';

-- =====================================================
-- TABLA: TRAMITE
-- =====================================================
CREATE TABLE tramite (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    ciudadano_id UUID NOT NULL,
    tipo_tramite_id UUID NOT NULL,
    estado VARCHAR(30) NOT NULL,
    fecha_solicitud TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ban_activo BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT fk_tramite_ciudadano
        FOREIGN KEY (ciudadano_id)
        REFERENCES ciudadano(id),

    CONSTRAINT fk_tramite_tipo_tramite
        FOREIGN KEY (tipo_tramite_id)
        REFERENCES tipo_tramite(id)
);

COMMENT ON TABLE tramite IS 'Trámites realizados por ciudadanos';