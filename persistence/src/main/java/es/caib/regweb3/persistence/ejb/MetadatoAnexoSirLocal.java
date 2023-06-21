package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.MetadatoAnexoSir;

import javax.ejb.Local;

@Local
public interface MetadatoAnexoSirLocal extends BaseEjb<MetadatoAnexoSir, Long> {

    String JNDI_NAME = "java:app/regweb3-persistence/MetadatoAnexoSirEJB";
}
