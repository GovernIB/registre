

-- 13/03/2015  Unique multiple de codigo asunto (jpernia)

alter table RWE_CODIGOASUNTO drop constraint RWE_CODIGOASUNTO_CODIGO_uk;
alter table RWE_CODIGOASUNTO add constraint RWE_CODASUN_CODIGO_TIPASUN_UK unique (CODIGO, TIPOASUNTO);