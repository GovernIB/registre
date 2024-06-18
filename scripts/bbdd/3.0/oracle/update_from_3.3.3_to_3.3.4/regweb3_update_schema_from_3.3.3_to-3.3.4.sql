DECLARE
interesado_id INTEGER;
   anexo_id INTEGER;
   anexoSir_id INTEGER;
   archivo_id INTEGER;
   hre_id INTEGER;
   hrs_id INTEGER;
   interesadoSir_id INTEGER;
   lopd_id INTEGER;
   notificacion_id INTEGER;
   oficina_id INTEGER;
   oficio_id INTEGER;
   organismo_id INTEGER;
   persona_id INTEGER;
   registroDetalle_id INTEGER;
   registroEntrada_id INTEGER;
   registroSalida_id INTEGER;
   registroSir_id INTEGER;
   trazabilidad_id INTEGER;
   trazabilidadSir_id INTEGER;
   usuario_id INTEGER;
   usuarioEntidad_id INTEGER;

BEGIN
    SELECT MAX (id) INTO interesado_id FROM RWE_INTERESADO; interesado_id := interesado_id + 1;
    SELECT MAX (id) INTO anexo_id FROM RWE_ANEXO; anexo_id := anexo_id + 1;
    SELECT MAX (id) INTO anexoSir_id FROM RWE_ANEXO_SIR; anexoSir_id := anexoSir_id + 1;
    SELECT MAX (id) INTO archivo_id FROM RWE_ARCHIVO; archivo_id := archivo_id + 1;
    SELECT MAX (id) INTO hre_id FROM RWE_HISTORICO_REGISTRO_ENTRADA; hre_id := hre_id + 1;
    SELECT MAX (id) INTO hrs_id FROM RWE_HISTORICO_REGISTRO_SALIDA; hrs_id := hrs_id + 1;
    SELECT MAX (id) INTO interesadoSir_id FROM RWE_INTERESADO_SIR; interesadoSir_id := interesadoSir_id + 1;
    SELECT MAX (id) INTO lopd_id FROM RWE_LOPD; lopd_id := lopd_id + 1;
    SELECT MAX (id) INTO notificacion_id FROM RWE_NOTIFICACION; notificacion_id := notificacion_id + 1;
    SELECT MAX (id) INTO oficina_id FROM RWE_OFICINA; oficina_id := oficina_id + 1;
    SELECT MAX (id) INTO oficio_id FROM RWE_OFICIO_REMISION; oficio_id := oficio_id + 1;
    SELECT MAX (id) INTO organismo_id FROM RWE_ORGANISMO; organismo_id := organismo_id + 1;
    SELECT MAX (id) INTO persona_id FROM RWE_PERSONA; persona_id := persona_id + 1;
    SELECT MAX (id) INTO registroDetalle_id FROM RWE_REGISTRO_DETALLE; registroDetalle_id := registroDetalle_id + 1;
    SELECT MAX (id) INTO registroEntrada_id FROM RWE_REGISTRO_ENTRADA; registroEntrada_id := registroEntrada_id + 1;
    SELECT MAX (id) INTO registroSalida_id FROM RWE_REGISTRO_SALIDA; registroSalida_id := registroSalida_id + 1;
    SELECT MAX (id) INTO registroSir_id FROM RWE_REGISTRO_SIR; registroSir_id := registroSir_id + 1;
    SELECT MAX (id) INTO trazabilidad_id FROM RWE_TRAZABILIDAD; trazabilidad_id := trazabilidad_id + 1;
    SELECT MAX (id) INTO trazabilidadSir_id FROM RWE_TRAZABILIDAD_SIR; trazabilidadSir_id := trazabilidadSir_id + 1;
    SELECT MAX (id) INTO usuario_id FROM RWE_USUARIO; usuario_id := usuario_id + 1;
    SELECT MAX (id) INTO usuarioEntidad_id FROM RWE_USUARIO_ENTIDAD; usuarioEntidad_id := usuarioEntidad_id + 1;

    EXECUTE IMMEDIATE 'CREATE SEQUENCE RWE_INTERESADO_SEQ START WITH ' || interesado_id || ' INCREMENT BY 1';
    EXECUTE IMMEDIATE 'CREATE SEQUENCE RWE_ANEXO_SEQ START WITH ' || anexo_id || ' INCREMENT BY 1';
    EXECUTE IMMEDIATE 'CREATE SEQUENCE RWE_ANEXOSIR_SEQ START WITH ' || anexoSir_id || ' INCREMENT BY 1';
    EXECUTE IMMEDIATE 'CREATE SEQUENCE RWE_ARCHIVO_SEQ START WITH ' || archivo_id || ' INCREMENT BY 1';
    EXECUTE IMMEDIATE 'CREATE SEQUENCE RWE_HRE_SEQ START WITH ' || hre_id || ' INCREMENT BY 1';
    EXECUTE IMMEDIATE 'CREATE SEQUENCE RWE_HRS_SEQ START WITH ' || hrs_id || ' INCREMENT BY 1';
    EXECUTE IMMEDIATE 'CREATE SEQUENCE RWE_INTERESADOSIR_SEQ START WITH ' || interesadoSir_id || ' INCREMENT BY 1';
    EXECUTE IMMEDIATE 'CREATE SEQUENCE RWE_LOPD_SEQ START WITH ' || lopd_id || ' INCREMENT BY 1';
    EXECUTE IMMEDIATE 'CREATE SEQUENCE RWE_NOTIFICACION_SEQ START WITH ' || notificacion_id || ' INCREMENT BY 1';
    EXECUTE IMMEDIATE 'CREATE SEQUENCE RWE_OFICINA_SEQ START WITH ' || oficina_id || ' INCREMENT BY 1';
    EXECUTE IMMEDIATE 'CREATE SEQUENCE RWE_OFICIOREMISION_SEQ START WITH ' || oficio_id || ' INCREMENT BY 1';
    EXECUTE IMMEDIATE 'CREATE SEQUENCE RWE_ORGANISMO_SEQ START WITH ' || organismo_id || ' INCREMENT BY 1';
    EXECUTE IMMEDIATE 'CREATE SEQUENCE RWE_PERSONA_SEQ START WITH ' || persona_id || ' INCREMENT BY 1';
    EXECUTE IMMEDIATE 'CREATE SEQUENCE RWE_REGISTRODETALLE_SEQ START WITH ' || registroDetalle_id || ' INCREMENT BY 1';
    EXECUTE IMMEDIATE 'CREATE SEQUENCE RWE_REGISTROENTRADA_SEQ START WITH ' || registroEntrada_id || ' INCREMENT BY 1';
    EXECUTE IMMEDIATE 'CREATE SEQUENCE RWE_REGISTROSALIDA_SEQ START WITH ' || registroSalida_id || ' INCREMENT BY 1';
    EXECUTE IMMEDIATE 'CREATE SEQUENCE RWE_REGISTROSIR_SEQ START WITH ' || registroSir_id || ' INCREMENT BY 1';
    EXECUTE IMMEDIATE 'CREATE SEQUENCE RWE_TRAZABILIDAD_SEQ START WITH ' || trazabilidad_id || ' INCREMENT BY 1';
    EXECUTE IMMEDIATE 'CREATE SEQUENCE RWE_TRAZABILIDADSIR_SEQ START WITH ' || trazabilidadSir_id || ' INCREMENT BY 1';
    EXECUTE IMMEDIATE 'CREATE SEQUENCE RWE_USUARIO_SEQ START WITH ' || usuario_id || ' INCREMENT BY 1';
    EXECUTE IMMEDIATE 'CREATE SEQUENCE RWE_USUARIOENTIDAD_SEQ START WITH ' || usuarioEntidad_id || ' INCREMENT BY 1';
END;
/