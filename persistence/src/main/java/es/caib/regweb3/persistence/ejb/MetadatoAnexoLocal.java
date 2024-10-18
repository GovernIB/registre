package es.caib.regweb3.persistence.ejb;


import es.caib.regweb3.model.Anexo;
import es.caib.regweb3.model.MetadatoAnexo;
import org.fundaciobit.genapp.common.i18n.I18NException;

import javax.ejb.Local;

@Local
public interface MetadatoAnexoLocal extends BaseEjb<MetadatoAnexo, Long> {

    String JNDI_NAME = "java:app/regweb3-persistence/MetadatoAnexoEJB";

    MetadatoAnexo guardarMetadatoAnexo(MetadatoAnexo metadatoAnexo, Anexo anexo) throws I18NException;
}
