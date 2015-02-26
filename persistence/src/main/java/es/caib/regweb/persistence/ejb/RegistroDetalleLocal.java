package es.caib.regweb.persistence.ejb;

import es.caib.regweb.model.RegistroDetalle;

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
public interface RegistroDetalleLocal extends BaseEjb<RegistroDetalle, Long> {
}
