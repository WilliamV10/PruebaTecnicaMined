-- =====================================================
-- PRUEBA TÉCNICA – SISTEMA GUBERNAMENTAL
-- Base de datos: PostgreSQL
-- Descripción:
--  - Manejo de ciudadanos y trámites
--  - Catálogos normalizados
--  - UUID como PK
--  - Soft delete (is_active)
--  - Auditoría con fechas de creación y modificación
--  - Trigger genérico para fecha_modificacion
-- =====================================================


-- =====================================================
-- EXTENSIÓN PARA UUID
-- =====================================================
CREATE EXTENSION IF NOT EXISTS "pgcrypto";


-- =====================================================
-- FUNCIÓN GENÉRICA DE AUDITORÍA
-- =====================================================
CREATE OR REPLACE FUNCTION fn_set_fecha_modificacion()
RETURNS TRIGGER AS $$
BEGIN
    NEW.fecha_modificacion = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;


-- =====================================================
-- CATÁLOGO: TIPO_DOCUMENTO
-- =====================================================
CREATE TABLE IF NOT EXISTS tipo_documento (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nombre VARCHAR(100) NOT NULL UNIQUE,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP,
    is_active BOOLEAN NOT NULL DEFAULT TRUE
);

COMMENT ON TABLE tipo_documento IS 'Catálogo de tipos de documentos de identificación';

DROP TRIGGER IF EXISTS trg_tipo_documento_mod ON tipo_documento;
CREATE TRIGGER trg_tipo_documento_mod
BEFORE UPDATE ON tipo_documento
FOR EACH ROW
EXECUTE FUNCTION fn_set_fecha_modificacion();


-- =====================================================
-- TABLA: CIUDADANO
-- =====================================================
CREATE TABLE IF NOT EXISTS ciudadano (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tipo_documento_id UUID NOT NULL,
    numero_documento VARCHAR(30) NOT NULL,
    nombre VARCHAR(150) NOT NULL,
    correo VARCHAR(150) NOT NULL,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT fk_ciudadano_tipo_documento
        FOREIGN KEY (tipo_documento_id)
        REFERENCES tipo_documento(id),

    CONSTRAINT uk_ciudadano_documento
        UNIQUE (tipo_documento_id, numero_documento),

    CONSTRAINT uk_ciudadano_correo
        UNIQUE (correo)
);

COMMENT ON TABLE ciudadano IS 'Registro de ciudadanos';

DROP TRIGGER IF EXISTS trg_ciudadano_mod ON ciudadano;
CREATE TRIGGER trg_ciudadano_mod
BEFORE UPDATE ON ciudadano
FOR EACH ROW
EXECUTE FUNCTION fn_set_fecha_modificacion();


-- =====================================================
-- CATÁLOGO: TIPO_TRAMITE
-- =====================================================
CREATE TABLE IF NOT EXISTS tipo_tramite (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nombre VARCHAR(100) NOT NULL UNIQUE,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP,
    is_active BOOLEAN NOT NULL DEFAULT TRUE
);

COMMENT ON TABLE tipo_tramite IS 'Catálogo de tipos de trámite';

DROP TRIGGER IF EXISTS trg_tipo_tramite_mod ON tipo_tramite;
CREATE TRIGGER trg_tipo_tramite_mod
BEFORE UPDATE ON tipo_tramite
FOR EACH ROW
EXECUTE FUNCTION fn_set_fecha_modificacion();


-- =====================================================
-- TABLA: TRAMITE
-- =====================================================
CREATE TABLE IF NOT EXISTS tramite (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    ciudadano_id UUID NOT NULL,
    tipo_tramite_id UUID NOT NULL,

    estado VARCHAR(20) NOT NULL,
    observacion VARCHAR(500),

    fecha_solicitud TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT fk_tramite_ciudadano
        FOREIGN KEY (ciudadano_id)
        REFERENCES ciudadano(id),

    CONSTRAINT fk_tramite_tipo_tramite
        FOREIGN KEY (tipo_tramite_id)
        REFERENCES tipo_tramite(id),

    CONSTRAINT chk_tramite_estado
        CHECK (estado IN ('PENDIENTE', 'APROBADO', 'RECHAZADO'))
);

COMMENT ON TABLE tramite IS 'Trámites realizados por ciudadanos';

-- Índice único parcial: Un ciudadano solo puede tener un trámite PENDIENTE por tipo
DROP INDEX IF EXISTS uk_tramite_ciudadano_tipo_pendiente;
CREATE UNIQUE INDEX uk_tramite_ciudadano_tipo_pendiente 
ON tramite (ciudadano_id, tipo_tramite_id) 
WHERE estado = 'PENDIENTE' AND is_active = TRUE;

COMMENT ON INDEX uk_tramite_ciudadano_tipo_pendiente IS 'Evita trámites duplicados pendientes del mismo tipo para un ciudadano';

DROP TRIGGER IF EXISTS trg_tramite_mod ON tramite;
CREATE TRIGGER trg_tramite_mod
BEFORE UPDATE ON tramite
FOR EACH ROW
EXECUTE FUNCTION fn_set_fecha_modificacion();
