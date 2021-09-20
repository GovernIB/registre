package es.caib.regweb3.persistence.ejb;


import es.caib.regweb3.model.Descarga;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;

/**
 * Created by Fundacio Bit
 *
 * @author earrivi
 * Date: 6/03/13
 */
@Local
@RolesAllowed({"RWE_SUPERADMIN"})
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
