package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.MetadatoRegistroEntrada;
import es.caib.regweb3.model.RegistroEntrada;
import org.fundaciobit.genapp.common.i18n.I18NException;

import javax.ejb.Local;

@Local
public interface MetadatoRegistroEntradaLocal extends BaseEjb<MetadatoRegistroEntrada, Long> {

    String JNDI_NAME = "java:app/regweb3-persistence/MetadatoRegistroEntradaEJB";

    MetadatoRegistroEntrada guardarMetadatoRegistroEntrada(MetadatoRegistroEntrada metadatoRegistroEntrada, RegistroEntrada registroEntrada) throws I18NException;
}
