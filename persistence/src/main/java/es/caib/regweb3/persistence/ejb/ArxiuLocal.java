package es.caib.regweb3.persistence.ejb;


import es.caib.arxiudigital.apirest.ApiArchivoDigital;
import es.caib.arxiudigital.apirest.facade.pojos.Expediente;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */
@Local
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI"})
public interface ArxiuLocal {

    /**
     *
     * @param expediente
     * @param apiArxiu
     * @throws Exception
     */
    void cerrarExpediente(Expediente expediente, ApiArchivoDigital apiArxiu, Long idEntidad) throws Exception;

    /**
     *
     * @param idEntidad
     * @param fechaInicio
     * @throws Exception
     */
    public void cerrarExpedientesScheduler(Long idEntidad, String fechaInicio) throws Exception;

}
