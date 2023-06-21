package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.MetadatoRegistroSalida;

import javax.ejb.Local;

@Local
public interface MetadatoRegistroSalidaLocal extends BaseEjb<MetadatoRegistroSalida, Long> {

    String JNDI_NAME = "java:app/regweb3-persistence/MetadatoRegistroSalidaEJB";
}
