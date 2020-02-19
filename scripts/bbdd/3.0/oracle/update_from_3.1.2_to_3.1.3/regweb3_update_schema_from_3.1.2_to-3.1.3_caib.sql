--Cambio de tipo de datos para campos varchar2 de 4000 caracteres
ALTER TABLE RWE_REGISTRO_DETALLE RENAME COLUMN expone TO expone2;
ALTER TABLE RWE_REGISTRO_DETALLE RENAME COLUMN solicita TO solicita2;

ALTER TABLE RWE_REGISTRO_DETALLE ADD (expone CLOB);
ALTER TABLE RWE_REGISTRO_DETALLE ADD (solicita CLOB);

alter table RWE_REGISTRO_DETALLE move lob (expone) store as RWE_REG_DET_EXPONE_lob (tablespace regweb_lob index RWE_REG_DET_EXPONE_lob_i);
alter table RWE_REGISTRO_DETALLE move lob (solicita) store as RWE_REG_DET_SOLICITA_lob (tablespace regweb_lob index RWE_REG_DET_SOLICITA_lob_i);

UPDATE RWE_REGISTRO_DETALLE SET expone=expone2;
UPDATE RWE_REGISTRO_DETALLE SET solicita=solicita2;
ALTER TABLE RWE_REGISTRO_DETALLE DROP COLUMN expone2;
ALTER TABLE RWE_REGISTRO_DETALLE DROP COLUMN solicita2;

ALTER INDEX RWE_REGISTRO_DETALLE_PK REBUILD;

--Cambio de tipo de datos para campos varchar2 de 4000 caracteres
ALTER TABLE RWE_REGISTRO_SIR RENAME COLUMN expone TO expone2;
ALTER TABLE RWE_REGISTRO_SIR RENAME COLUMN solicita TO solicita2;

ALTER TABLE RWE_REGISTRO_SIR ADD (expone CLOB);
ALTER TABLE RWE_REGISTRO_SIR ADD (solicita CLOB);

alter table RWE_REGISTRO_SIR move lob (expone) store as RWE_REG_SIR_EXPONE_lob (tablespace regweb_lob index RWE_REG_SIR_EXPONE_lob_i);
alter table RWE_REGISTRO_SIR move lob (solicita) store as RWE_REG_SIR_SOLICITA_lob (tablespace regweb_lob index RWE_REG_SIR_SOLICITA_lob_i);

UPDATE RWE_REGISTRO_SIR SET expone=expone2;
UPDATE RWE_REGISTRO_SIR SET solicita=solicita2;
ALTER TABLE RWE_REGISTRO_SIR DROP COLUMN expone2;
ALTER TABLE RWE_REGISTRO_SIR DROP COLUMN solicita2;

ALTER INDEX RWE_REGISTRO_SIR_PK REBUILD;

