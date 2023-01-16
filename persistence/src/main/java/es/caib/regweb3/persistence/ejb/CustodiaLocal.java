package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Cola;
import org.fundaciobit.genapp.common.i18n.I18NException;

import javax.ejb.Local;
import java.util.List;

/**
 * @author earrivi
 * Date: 04/06/21
 */
@Local
public interface CustodiaLocal {

    String JNDI_NAME = "java:app/regweb3-persistence/CustodiaEJB";

    /**
     * @param elemento
     * @param idEntidad
     * @param tipoIntegracon
     * @throws I18NException
     */
    Boolean custodiarJustificanteEnCola(Cola elemento, Long idEntidad, Long tipoIntegracon) throws I18NException;

    /**
     * @param idEntidad
     * @throws I18NException
     */
    void custodiarJustificantesEnCola(Long idEntidad, List<Cola> elementos) throws I18NException;

}

