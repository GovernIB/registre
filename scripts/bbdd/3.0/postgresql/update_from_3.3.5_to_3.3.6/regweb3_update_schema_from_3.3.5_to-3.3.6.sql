-- Nuevos campos en RWE_REGISTRO_DETALLE PARA FUNCIONARIOS HABILITADOS
ALTER TABLE rwe_registro_detalle ADD COLUMN codigo_func_hab VARCHAR(256);
ALTER TABLE rwe_registro_detalle ADD COLUMN nombre_func_hab VARCHAR(512);
ALTER TABLE rwe_registro_detalle ADD COLUMN nif_func_hab VARCHAR(256);
