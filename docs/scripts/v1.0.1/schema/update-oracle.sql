--
-- Nova columna introduida per gestio de numero d'entrades en cas que s'empri funcionalitat 012.
--
-- Oracle (varchar2 enlloc de varchar):
ALTER TABLE BZENTRA060 ADD ENT_NUMREG VARCHAR2(5) NOT NULL;
