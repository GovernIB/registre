package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Cola;

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
     * @throws Exception
     */
    Boolean custodiarJustificanteEnCola(Cola elemento, Long idEntidad, Long tipoIntegracon) throws Exception;

    /**
     * @param idEntidad
     * @throws Exception
     */
    void custodiarJustificantesEnCola(Long idEntidad, List<Cola> elementos) throws Exception;

}

