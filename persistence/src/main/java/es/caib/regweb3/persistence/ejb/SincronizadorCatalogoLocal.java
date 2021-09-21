package es.caib.regweb3.persistence.ejb;


import es.caib.regweb3.model.Descarga;

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
     * @throws Exception
     */
    Descarga sincronizarCatalogo() throws Exception;

    /**
     * @return
     * @throws Exception
     */
    Descarga actualizarCatalogo() throws Exception;
}
