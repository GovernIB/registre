--
-- Nova columna introduida per gestio de numero d'entrades en cas que s'empri funcionalitat 012.
--
-- postgresql / mysql / sqlserver / db2:
ALTER TABLE BZENTRA060 ADD ENT_NUMREG VARCHAR(5) NOT NULL;
