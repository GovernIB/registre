package es.caib.regweb3.persistence.ejb;


import es.caib.regweb3.model.Descarga;
import org.fundaciobit.genapp.common.i18n.I18NException;

import javax.ejb.Local;

/**
 * Created by Fundacio Bit
 *
 * @author earrivi
 * Date: 6/03/13
 */
@Local
public interface SincronizadorCatalogoLocal {

    String JNDI_NAME = "java:app/regweb3-persistence/SincronizadorCatalogoEJB";


    /**
     * @return
     * @throws I18NException
     */
    Descarga sincronizarCatalogo() throws I18NException;

    /**
     * @return
     * @throws I18NException
     */
    Descarga actualizarCatalogo() throws I18NException;
}
