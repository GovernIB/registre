package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Cola;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;

/**
 *
 * @author earrivi
 * Date: 04/06/21
 */
@Local
@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI"})
public interface CustodiaLocal {

    /**
     *
     * @param elemento
     * @param idEntidad
     * @param tipoIntegracon
     * @throws Exception
     */
    Boolean custodiarJustificanteEnCola(Cola elemento, Long idEntidad, Long tipoIntegracon) throws Exception;

    /**
     *
     * @param idEntidad
     * @throws Exception
     */
    void custodiarJustificantesEnCola(Long idEntidad) throws Exception;

}

