package es.caib.regweb3.persistence.ejb;


import es.caib.regweb3.model.MetadatoAnexo;

import javax.ejb.Local;

@Local
public interface MetadatoAnexoLocal extends BaseEjb<MetadatoAnexo, Long> {

    String JNDI_NAME = "java:app/regweb3-persistence/MetadatoAnexoEJB";
}
