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
    void purgarIntegraciones() throws Exception;

    /**
     *
     * @throws Exception
     */
    void purgarAnexosSir() throws Exception;

    /**
     *
     * @throws Exception
     */
    void reintentarEnviosSinConfirmacion() throws Exception;

    /**
     *
     * @throws Exception
     */
    void reintentarEnviosConError() throws Exception;

    /**
     *
     * @throws Exception
     */
    void reiniciarContadoresEntidad() throws Exception;

    /**
     * Inicia la distribución de los registros en cola de cada entidad.
     *
     * @throws Exception
     */
    void distribuirRegistrosEnCola() throws Exception;

    /**
     *
     * @throws Exception
     */
    void cerrarExpedientes() throws Exception;

}

