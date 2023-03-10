package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.IRegistro;
import org.fundaciobit.genapp.common.i18n.I18NException;

import javax.ejb.Local;

/**
 * @author earrivi
 * Date: 04/06/21
 */
@Local
public interface CarpetaLocal {

    String JNDI_NAME = "java:app/regweb3-persistence/CarpetaEJB";

    /**
     * @param registro
     * @param idEntidad
     * @throws I18NException
     */
    void enviarNotificacionCarpeta(IRegistro registro, Long idEntidad) throws I18NException;


}

