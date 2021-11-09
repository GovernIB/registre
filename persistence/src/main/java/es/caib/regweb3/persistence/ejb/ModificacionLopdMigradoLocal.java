package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.ModificacionLopdMigrado;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author jpernia
 * Date: 09/12/14
 */
@Local
@RolesAllowed({"RWE_USUARI"})
public interface ModificacionLopdMigradoLocal extends BaseEjb<ModificacionLopdMigrado, Long> {

    /**
     * Devuelve el registro Lopd de un Registro Migrado concreto
     * @param numRegistroMigrado
     * @return
     * @throws Exception
     */
    List<ModificacionLopdMigrado> getByRegistroMigrado(Long numRegistroMigrado) throws Exception;

}
