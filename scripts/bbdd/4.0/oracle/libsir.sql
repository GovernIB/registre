-- Insertar nueva Oficina
INSERT INTO LI_OFICINA(cd_oficina,cd_en_rg_procesa, num_secuencia_oficina, anyo, it_borrado) VALUES (LI_OFICINA_SEQ.nextVal, 'O00015973',0,2024,1);

-- Grants para todas las tablas Y SECUENCIAS al usuario WWW
BEGIN
    FOR x IN (SELECT * FROM user_tables)

    LOOP

     EXECUTE IMMEDIATE 'GRANT SELECT, INSERT, DELETE, UPDATE ON ' || x.table_name || ' TO WWW_SIR_LIB_INTER';

    END LOOP;

    FOR R IN (SELECT sequence_owner, sequence_name FROM all_sequences WHERE sequence_owner='SIR_LIB_INTER') LOOP
              EXECUTE IMMEDIATE 'grant select on '||R.sequence_owner||'.'||R.sequence_name||' to WWW_SIR_LIB_INTER';
    END LOOP;

END;
/

