package es.caib.regweb3.persistence.ejb;

import javax.ejb.Local;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */
@Local
/*@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI"})*/
public interface SchedulerLocal {

    /**
     *
     * @throws Exception
     */
    public void reintentarEnviosSinConfirmacion() throws Exception;

    /**
     *
     * @throws Exception
     */
    public void reintentarEnviosConError() throws Exception;

}

