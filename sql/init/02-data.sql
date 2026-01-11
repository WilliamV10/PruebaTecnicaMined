-- =====================================================
-- DATOS INICIALES - TIPOS DE DOCUMENTO
-- =====================================================
INSERT INTO tipo_documento (nombre) 
VALUES ('DUI')
ON CONFLICT (nombre) DO NOTHING;

INSERT INTO tipo_documento (nombre) 
VALUES ('Pasaporte')
ON CONFLICT (nombre) DO NOTHING;

INSERT INTO tipo_documento (nombre) 
VALUES ('NIT')
ON CONFLICT (nombre) DO NOTHING;


-- =====================================================
-- DATOS INICIALES - TIPOS DE TRAMITE
-- =====================================================
INSERT INTO tipo_tramite (nombre) 
VALUES ('Solicitud de Partida de Nacimiento')
ON CONFLICT (nombre) DO NOTHING;

INSERT INTO tipo_tramite (nombre) 
VALUES ('Solicitud de Reposición de DUI')
ON CONFLICT (nombre) DO NOTHING;

INSERT INTO tipo_tramite (nombre) 
VALUES ('Solicitud de Constancia Penal')
ON CONFLICT (nombre) DO NOTHING;


-- =====================================================
-- VERIFICACIÓN
-- =====================================================
DO $$
BEGIN
    RAISE NOTICE '=== Tipos de Documento insertados ===';
END $$;

SELECT id, nombre, fecha_creacion, is_active FROM tipo_documento;

DO $$
BEGIN
    RAISE NOTICE '=== Tipos de Trámite insertados ===';
END $$;

SELECT id, nombre, fecha_creacion, is_active FROM tipo_tramite;
