package es.caib.regweb3.persistence.ejb;


import es.caib.regweb3.model.RegistroEntrada;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;


/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 *  @author anadal
 *         Date: 16/01/14
 */
@Local
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI","RWE_WS_ENTRADA","RWE_WS_SALIDA"})
public interface RegistroEntradaCambiarEstadoLocal extends BaseEjb<RegistroEntrada, Long> {

    String JNDI_NAME = "java:app/regweb3-persistence/RegistroEntradaCambiarEstadoEJB";


    /**
     * Cambia el estado de un RegistroEntrada
     * @param idRegistro
     * @param idEstado
     * @throws Exception
     */
    void cambiarEstado(Long idRegistro, Long idEstado) throws Exception;

}
