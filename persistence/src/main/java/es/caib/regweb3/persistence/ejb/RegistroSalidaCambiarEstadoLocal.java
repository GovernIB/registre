package es.caib.regweb3.persistence.ejb;


import es.caib.regweb3.model.RegistroSalida;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;



/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 *  @author anadal
 * Date: 16/01/14
 */
@Local
public interface RegistroSalidaCambiarEstadoLocal extends BaseEjb<RegistroSalida, Long> {

    /**
     * Cambiar el estado del registro
     * @param idRegistro
     * @param idEstado
     * @throws Exception
     */
    void cambiarEstado(Long idRegistro, Long idEstado) throws Exception;


}
