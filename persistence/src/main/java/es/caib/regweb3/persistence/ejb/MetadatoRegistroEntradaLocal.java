package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.MetadatoRegistroEntrada;

import javax.ejb.Local;

@Local
public interface MetadatoRegistroEntradaLocal extends BaseEjb<MetadatoRegistroEntrada, Long> {

    String JNDI_NAME = "java:app/regweb3-persistence/MetadatoRegistroEntradaEJB";
}
