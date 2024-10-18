package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.MetadatoRegistroSalida;
import es.caib.regweb3.model.RegistroSalida;
import org.fundaciobit.genapp.common.i18n.I18NException;

import javax.ejb.Local;

@Local
public interface MetadatoRegistroSalidaLocal extends BaseEjb<MetadatoRegistroSalida, Long> {

    String JNDI_NAME = "java:app/regweb3-persistence/MetadatoRegistroSalidaEJB";

    MetadatoRegistroSalida guardarMetadatoRegistroSalida(MetadatoRegistroSalida metadatoRegistroSalida, RegistroSalida registroSalida) throws I18NException;
}
