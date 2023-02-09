package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.MetadatoRegistroSir;

import javax.ejb.Local;

@Local
public interface MetadatoRegistroSirLocal extends BaseEjb<MetadatoRegistroSir, Long> {

    String JNDI_NAME = "java:app/regweb3-persistence/MetadatoRegistroSirEJB";
}
